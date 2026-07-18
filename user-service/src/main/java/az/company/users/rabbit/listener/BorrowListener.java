package az.company.users.rabbit.listener;

import az.company.users.dao.entity.BorrowHistoryEntity;
import az.company.users.dao.repository.BorrowHistoryRepository;
import az.company.users.dao.repository.UserRepository;
import az.company.users.exception.NotFoundException;
import az.company.users.model.dto.BorrowEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static az.company.users.config.RabbitMQConfig.*;
import static az.company.users.exception.enums.ErrorStatus.*;
import static java.lang.String.format;

@Component
@RequiredArgsConstructor
@Transactional
public class BorrowListener {
    private final BorrowHistoryRepository borrowHistoryRepository;
    private final UserRepository userRepository;

    @RabbitListener(queues = BORROW_CREATED_QUEUE)
    public void addBorrowToBorrowHistory(BorrowEvent borrowEvent) {
        var userEntity = userRepository.findById(borrowEvent.getUserId())
                .orElseThrow(
                        () -> new NotFoundException(
                                USER_NOT_FOUND.name(),
                                format(USER_NOT_FOUND.getMessage(), borrowEvent.getUserId())
                        )
                );
        var borrowHistoryEntity = BorrowHistoryEntity
                .builder()
                .user(userEntity)
                .bookId(borrowEvent.getBookId())
                .bookTitle(borrowEvent.getBookTitle())
                .borrowedAt(borrowEvent.getBorrowedAt())
                .returnedAt(borrowEvent.getReturnedAt())
                .status(borrowEvent.getStatus())
                .build();
        borrowHistoryRepository.save(borrowHistoryEntity);
    }

@RabbitListener(queues = BORROW_UPDATE_QUEUE)
    public void updateBorrowInBorrowHistory(BorrowEvent borrowEvent) {
        var borrowHistoryEntity = borrowHistoryRepository.findByBookIdAndUserId(borrowEvent.getBookId(), borrowEvent.getUserId())
                .orElseThrow(
                        () -> new NotFoundException(
                                BORROW_NOT_FOUND.name(),
                                format(BORROW_NOT_FOUND.getMessage(), borrowEvent.getBookId(), borrowEvent.getUserId())
                        )
                );
        borrowHistoryEntity.setReturnedAt(borrowEvent.getReturnedAt());
        borrowHistoryEntity.setStatus(borrowEvent.getStatus());
    }

}