package az.company.books.scheduler;

import az.company.books.dao.entity.BorrowEntity;
import az.company.books.dao.repository.BorrowRepository;
import az.company.books.model.dto.BorrowEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.util.UUID;

import static az.company.books.config.RabbitMQConfig.BORROW_UPDATE_EXCHANGE;
import static az.company.books.config.RabbitMQConfig.BORROW_UPDATE_ROUTING_KEY;
import static az.company.books.model.enums.BorrowStatus.OVERDUE;

@RequiredArgsConstructor
public class BorrowScheduler {
    private final BorrowRepository borrowRepository;
    private final RabbitTemplate rabbitTemplate;

    //region check borrow status
    @Scheduled(cron = "0 0 0 * * *",zone = "Asia/Baku")
    public void checkBorrowStatus() {
        var list = borrowRepository.findAll().stream()
                .filter(
                        borrow ->
                                borrow.getDueDate().isBefore(LocalDate.now()) && borrow.getReturnedAt() == null
                ).toList();

        for (var borrow : list) {

            borrow.setStatus(OVERDUE);
            BorrowEvent event = getBorrowEvent(borrow);
            event.setUserId(borrow.getUserId());
            rabbitTemplate.convertAndSend(
                    BORROW_UPDATE_EXCHANGE,
                    BORROW_UPDATE_ROUTING_KEY,
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
    //endregion
}
