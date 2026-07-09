package az.company.books.model.response;

import az.company.books.model.enums.BorrowStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BorrowResponse {
    private Long id;
    private Long userId;
    private Long bookId;
    private LocalDateTime borrowedAt;
    private LocalDate dueDate;
    private LocalDateTime returnedAt;
    private BorrowStatus status;

}
