package az.company.books.model.dto;

import az.company.books.model.enums.BorrowStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BorrowEvent {
    private String id;
    private Long userId;
    private Long bookId;
    private String bookTitle;
    private LocalDateTime borrowedAt;
    private LocalDateTime returnedAt;
    private BorrowStatus status;
}
