package az.company.books.service;

import az.company.books.dao.entity.BorrowEntity;
import az.company.books.dao.repository.BookRepository;
import az.company.books.dao.repository.BorrowRepository;
import az.company.books.exception.BookAlreadyBorrowedException;
import az.company.books.exception.ConflictException;
import az.company.books.exception.NotFoundException;

import static az.company.books.config.RabbitMQConfig.*;
import static az.company.books.exception.enums.ErrorStatus.*;
import static az.company.books.mapper.BorrowMapper.mapBorrowEntityToBorrowResponse;
import static az.company.books.model.enums.BorrowStatus.*;
import static java.lang.String.format;
import static java.time.LocalDateTime.now;

import az.company.books.mapper.BorrowMapper;
import az.company.books.model.dto.BorrowEvent;
import az.company.books.model.request.BorrowBookRequest;
import az.company.books.model.response.BorrowResponse;
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Transactional

public class BorrowService {
    private final BorrowRepository borrowRepository;
    private final BookRepository bookRepository;
    private final RabbitTemplate rabbitTemplate;

    // region borrow Method
    public BorrowResponse borrow(BorrowBookRequest borrowBookRequest, Long id) {
        var bookEntity = bookRepository.findById(borrowBookRequest.getBookId()).orElseThrow(() -> new NotFoundException(
                BOOK_NOT_FOUND.name(),
                format(BOOK_NOT_FOUND.getMessage(), borrowBookRequest.getBookId())
        ));
        if (borrowRepository.findByBookIdAndUserId(borrowBookRequest.getBookId(), id).isPresent()  && borrowRepository.findByBookIdAndUserId(borrowBookRequest.getBookId(), id).get().getStatus() == BORROWED) {
            throw new BookAlreadyBorrowedException(
                    BORROW_ALREADY_EXISTS.name(),
                    format(BORROW_ALREADY_EXISTS.getMessage(), bookEntity.getTitle())
            );
        }
        if (bookEntity.getAvailableCopies() == 0) {
            throw new ConflictException(
                    CONFLICT.name(),
                    format(CONFLICT.getMessage(), borrowBookRequest.getBookId())
            );
        }


        bookEntity.setAvailableCopies(bookEntity.getAvailableCopies() - 1);
        var borrowEntity = BorrowEntity
                .builder()
                .userId(id)
                .book(bookEntity)
                .borrowedAt(now())
                .dueDate(LocalDate.now().plusDays(14))
                .status(BORROWED)
                .build();

        borrowRepository.save(borrowEntity);
        var event = getBorrowEvent(borrowEntity);
        rabbitTemplate.convertAndSend(
                BORROW_EXCHANGE,
                BORROW_ROUTING_KEY,
                event
        );
        return mapBorrowEntityToBorrowResponse(borrowEntity);

    }
// endregion

    // region return book
    public void returnBook(Long borrowId, Long userId) {
        var borrowEntity = borrowRepository.findById(borrowId).orElseThrow(() -> new NotFoundException(
                BORROW_NOT_FOUND.name(),
                format(BORROW_NOT_FOUND.getMessage(), borrowId)
        ));
        var bookEntity = bookRepository.findById(borrowEntity.getBook().getId()).orElseThrow(() -> new NotFoundException(
                BOOK_NOT_FOUND.name(),
                format(BOOK_NOT_FOUND.getMessage(), borrowEntity.getBook().getId())
        ));

        bookEntity.setAvailableCopies(bookEntity.getAvailableCopies() + 1);
        borrowEntity.setStatus(RETURNED);
        borrowEntity.setReturnedAt(now());

        var event = getBorrowEvent(borrowEntity);
        event.setUserId(userId);
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
    @Scheduled(cron = "10 * * * * *",zone = "Asia/Baku")
    public void checkBorrowStatus() {
        var list = borrowRepository.findAll().stream()
                .filter(
                        borrow ->
                                borrow.getDueDate().isBefore(LocalDate.now()) && borrow.getReturnedAt() == null
                ).toList();

        for (var borrow : list) {

            borrow.setStatus(OVERDUE);
            var event = getBorrowEvent(borrow);
event.setUserId(borrow.getUserId());
            rabbitTemplate.convertAndSend(
                    BORROW_EXCHANGE,
                    BORROW_ROUTING_KEY,
                    event
            );
        }
    }




    private static BorrowEvent getBorrowEvent(BorrowEntity borrow) {
        return BorrowEvent.builder()
                .id(UUID.randomUUID().toString())
                .bookId(borrow.getBook().getId())
                .bookTitle(borrow.getBook().getTitle())
                .borrowedAt(borrow.getBorrowedAt())
                .returnedAt(borrow.getReturnedAt())
                .status(borrow.getStatus())
                .build();

    }

    public Page<BorrowResponse> getBorrowsByUserId(Long userId, Pageable pageable) {
        return borrowRepository.findByUserId(userId, pageable).map(
                BorrowMapper::mapBorrowEntityToBorrowResponse
        );
    }
    //endregion
}

