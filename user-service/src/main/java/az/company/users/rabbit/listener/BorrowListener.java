package az.company.users.rabbit.listener;

import az.company.users.dao.entity.BorrowHistoryEntity;
import az.company.users.dao.repository.BorrowHistoryRepository;
import az.company.users.dao.repository.UserRepository;
import az.company.users.exception.NotFoundException;
import az.company.users.model.dto.BorrowEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static az.company.users.config.RabbitMQConfig.*;
import static az.company.users.exception.enums.ErrorStatus.*;
import static java.lang.String.format;

@Component
@RequiredArgsConstructor
public class BorrowListener {
    private final BorrowHistoryRepository borrowHistoryRepository;
    private final UserRepository userRepository;

    @RabbitListener(queues = BORROW_HISTORY_QUEUE)
    public void listen(BorrowEvent borrowEvent) {
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
}
