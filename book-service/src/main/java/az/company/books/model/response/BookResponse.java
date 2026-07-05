package az.company.books.model.response;

import az.company.books.model.enums.BookStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookResponse {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private String description;
    private Integer totalCopies;
    private Integer availableCopies;
    private Year publishedYear;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BookStatus status;
    private String categoryName;

}
