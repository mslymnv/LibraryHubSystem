package az.company.books.service.concrete;

import az.company.books.dao.entity.BorrowEntity;
import az.company.books.dao.repository.BookRepository;
import az.company.books.dao.repository.BorrowRepository;
import az.company.books.exception.BookAlreadyBorrowedException;
import az.company.books.exception.ConflictException;
import az.company.books.exception.NotFoundException;
import az.company.books.mapper.BorrowMapper;
import az.company.books.model.dto.BorrowEvent;
import az.company.books.model.request.BorrowBookRequest;
import az.company.books.model.response.BorrowResponse;
import az.company.books.service.abstraction.BorrowService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

import static az.company.books.config.RabbitMQConfig.*;
import static az.company.books.config.RabbitMQConfig.BORROW_UPDATE_EXCHANGE;
import static az.company.books.config.RabbitMQConfig.BORROW_UPDATE_ROUTING_KEY;
import static az.company.books.exception.enums.ErrorStatus.*;
import static az.company.books.exception.enums.ErrorStatus.BOOK_NOT_FOUND;
import static az.company.books.model.enums.BorrowStatus.*;
import static java.lang.String.format;
import static java.time.LocalDateTime.now;

@Service
@Transactional
@RequiredArgsConstructor
public class BorrowServiceHandler implements BorrowService {
    private final BorrowRepository borrowRepository;
    private final BookRepository bookRepository;
    private final RabbitTemplate rabbitTemplate;
    private final BorrowMapper borrowMapper;

    // region borrow Method
    @Override
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
        event.setUserId(id);
        rabbitTemplate.convertAndSend(
                BORROW_CREATED_EXCHANGE,
                BORROW_CREATED_ROUTING_KEY,
                event
        );
        return borrowMapper.mapBorrowEntityToBorrowResponse(borrowEntity);

    }
// endregion

    // region return book
    @Override
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
                BORROW_UPDATE_EXCHANGE,
                BORROW_UPDATE_ROUTING_KEY,
                event
        );

    }
    // endregion

    // region get borrows
    @Override
    public Page<BorrowResponse> getBorrows(Pageable pageable) {
        return borrowRepository.findAll(pageable).map(
                borrowMapper::mapBorrowEntityToBorrowResponse
        );

    }
    //endregion




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
                borrowMapper::mapBorrowEntityToBorrowResponse
        );
    }

}
