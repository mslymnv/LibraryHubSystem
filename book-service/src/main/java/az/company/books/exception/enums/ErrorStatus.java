package az.company.books.exception.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorStatus {
    CATEGORY_NOT_FOUND("Category not found with id: %s"),
    BOOK_NOT_FOUND("Book not found with id: %s"),
    USER_NOT_FOUND("User not found with id: %s"),
    CONFLICT("Avialable copies aren't now with this book id: %s");

    private final String message;

}
