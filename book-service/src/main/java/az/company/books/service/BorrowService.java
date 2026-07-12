package az.company.books.service;

import az.company.books.dao.entity.BorrowEntity;
import az.company.books.dao.repository.BookRepository;
import az.company.books.dao.repository.BorrowRepository;
import az.company.books.exception.ConflictException;
import az.company.books.exception.NotFoundException;

import static az.company.books.config.RabbitMQConfig.*;
import static az.company.books.exception.enums.ErrorStatus.*;
import static az.company.books.model.enums.BorrowStatus.*;
import static java.lang.String.format;
import static java.time.LocalDateTime.now;

import az.company.books.mapper.BorrowMapper;
import az.company.books.model.dto.BorrowEvent;
import az.company.books.model.request.BorrowBookRequest;
import az.company.books.model.response.BorrowResponse;
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.support.SecurityWebApplicationContextUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class BorrowService {
    private final BorrowRepository borrowRepository;
    private final BookRepository bookRepository;
    private final RabbitTemplate rabbitTemplate;

    // region borrow Method
    @Transactional
    public void borrow(BorrowBookRequest borrowBookRequest,Long id) {
        var bookEntity = bookRepository.findById(borrowBookRequest.getBookId()).orElseThrow(() -> new NotFoundException(
                BOOK_NOT_FOUND.name(),
                format(BOOK_NOT_FOUND.getMessage(), borrowBookRequest.getBookId())
        ));
        if (bookEntity.getAvailableCopies() == 0) {
            throw new ConflictException(
                    CONFLICT.name(),
                    format(CONFLICT.getMessage(), borrowBookRequest.getBookId())
            );
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         id = (Long) authentication.getPrincipal();

        bookEntity.setAvailableCopies(bookEntity.getAvailableCopies() - 1);
        bookRepository.save(bookEntity);
        var borrowEntity = BorrowEntity
                .builder()
                .userId(id)
                .book(bookEntity)
                .borrowedAt(now())
                .dueDate(LocalDate.now().plusDays(14))
                .status(BORROWED)
                .build();

        borrowRepository.save(borrowEntity);
        BorrowEvent event = BorrowEvent.builder()
                .id(UUID.randomUUID().toString())
                .userId(borrowEntity.getUserId())
                .bookId(borrowEntity.getBook().getId())
                .bookTitle(borrowEntity.getBook().getTitle())
                .borrowedAt(borrowEntity.getBorrowedAt())
                .returnedAt(borrowEntity.getReturnedAt())
                .status(borrowEntity.getStatus())
                .build();
        rabbitTemplate.convertAndSend(
                BORROW_EXCHANGE,
                BORROW_ROUTING_KEY,
                event
        );

    }
// endregion

    // region return book
    @Transactional
    public void returnBook(Long bookId,Long userId) {
        var borrowEntity = borrowRepository.findById(bookId).orElseThrow(() -> new NotFoundException(
                BOOK_NOT_FOUND.name(),
                format(BOOK_NOT_FOUND.getMessage(), bookId)
        ));
        var bookEntity = bookRepository.findById(borrowEntity.getBook().getId()).orElseThrow(() -> new NotFoundException(
                BOOK_NOT_FOUND.name(),
                format(BOOK_NOT_FOUND.getMessage(), borrowEntity.getBook().getId())
        ));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userId = (Long) authentication.getPrincipal();
        bookEntity.setAvailableCopies(bookEntity.getAvailableCopies() + 1);
        bookRepository.save(bookEntity);
        borrowEntity.setStatus(RETURNED);
        borrowEntity.setReturnedAt(now());
        borrowRepository.save(borrowEntity);

        BorrowEvent event = BorrowEvent.builder()
                .id(UUID.randomUUID().toString())
                .userId(borrowEntity.getUserId())
                .bookId(borrowEntity.getBook().getId())
                .bookTitle(borrowEntity.getBook().getTitle())
                .borrowedAt(borrowEntity.getBorrowedAt())
                .returnedAt(borrowEntity.getReturnedAt())
                .status(borrowEntity.getStatus())
                .build();
        rabbitTemplate.convertAndSend(
                BORROW_EXCHANGE,
                BORROW_ROUTING_KEY,
                event
        );

    }
    // endregion

    // region get borrows
    public Page<BorrowResponse> getBorrows(Pageable pageable) {
        return borrowRepository.findAll(pageable).map(
                BorrowMapper::mapBorrowEntityToBorrowResponse
        );

    }
    //endregion

    //region check borrow status
    public void checkBorrowStatus() {
        var list = borrowRepository.findAll();
        for (var borrow : list) {
            if (borrow.getDueDate().isBefore(LocalDate.now()) && borrow.getReturnedAt() == null) {
                borrow.setStatus(OVERDUE);
                borrowRepository.save(borrow);
                var event = BorrowEvent.builder()
                        .id(UUID.randomUUID().toString())
                        .userId(borrow.getUserId())
                        .bookId(borrow.getBook().getId())
                        .bookTitle(borrow.getBook().getTitle())
                        .borrowedAt(borrow.getBorrowedAt())
                        .returnedAt(borrow.getReturnedAt())
                        .status(borrow.getStatus())
                        .build();

                rabbitTemplate.convertAndSend(
                        BORROW_EXCHANGE,
                        BORROW_ROUTING_KEY,
                        event
                );
            }
        }


    }
    public Page<BorrowResponse> getBorrowsByUserId(Long userId, Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userId = (Long) authentication.getPrincipal();
        return borrowRepository.findByUserId(userId, pageable).map(
                BorrowMapper::mapBorrowEntityToBorrowResponse
        );
    }
    //endregion
}

