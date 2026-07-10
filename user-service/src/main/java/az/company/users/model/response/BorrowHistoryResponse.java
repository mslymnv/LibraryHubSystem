package az.company.users.model.response;

import az.company.users.model.enums.BorrowStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BorrowHistoryResponse {
    private Long id;
    private Long bookId;
    private String bookTitle;
    private LocalDateTime borrowedAt;
    private LocalDateTime returnedAt;
    private BorrowStatus status;
}
