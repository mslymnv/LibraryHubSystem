package az.company.users.dao.entity;

import az.company.users.model.enums.BorrowStatus;
import jakarta.persistence.*;
import lombok.*;

import javax.annotation.processing.Generated;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "borrow_history")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BorrowHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long bookId;
    @Column(nullable = false)
    private String bookTitle;
    @Column(nullable = false)
    private LocalDateTime borrowedAt;
    private LocalDateTime returnedAt;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BorrowStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BorrowHistoryEntity that = (BorrowHistoryEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
