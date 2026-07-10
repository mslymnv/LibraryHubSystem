package az.company.books.dao.entity;

import az.company.books.model.enums.BorrowStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "borrows")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BorrowEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long userId;
    @Column(nullable = false)
    private LocalDateTime borrowedAt;
    @Column(nullable = false)
    private LocalDate dueDate;
    private LocalDateTime returnedAt;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BorrowStatus status;

    @ManyToOne()
    @JoinColumn(name = "book_id")
    private BookEntity book;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BorrowEntity that = (BorrowEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
