package az.company.books.mapper;

import az.company.books.dao.entity.BookEntity;
import az.company.books.dao.entity.CategoryEntity;
import az.company.books.model.enums.BookStatus;
import az.company.books.model.request.CreateBookRequest;
import az.company.books.model.response.BookResponse;
import org.hibernate.validator.constraints.ISBN;

import java.time.LocalDateTime;
import java.util.UUID;

import static az.company.books.model.enums.BookStatus.ACTIVE;
import static java.time.LocalDateTime.now;

public class BookMapper {
    public static BookEntity mapBookRequestToBookEntity(CreateBookRequest createBookRequest) {
        return BookEntity.builder()
                .title(createBookRequest.getTitle())
                .author(createBookRequest.getAuthor())
                .isbn(UUID.randomUUID().toString())
                .description(createBookRequest.getDescription())
                .totalCopies(createBookRequest.getTotalCopies())
                .availableCopies(createBookRequest.getTotalCopies())
                .publishedYear(createBookRequest.getPublishedYear())
                .status(ACTIVE)
                .createdAt(now())
                .updatedAt(now())
                .build();


    }

    public static BookResponse mapBookEntityToBookResponse(BookEntity bookEntity) {
        return BookResponse.builder()
                .id(bookEntity.getId())
                .title(bookEntity.getTitle())
                .author(bookEntity.getAuthor())
                .isbn(bookEntity.getIsbn())
                .description(bookEntity.getDescription())
                .totalCopies(bookEntity.getTotalCopies())
                .availableCopies(bookEntity.getAvailableCopies())
                .publishedYear(bookEntity.getPublishedYear())
                .createdAt(bookEntity.getCreatedAt())
                .updatedAt(bookEntity.getUpdatedAt())
                .status(bookEntity.getStatus())
                .categoryName(bookEntity.getCategory().getName())
                .build();
    }

}
