package az.company.books.exception.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorStatus {
    CATEGORY_NOT_FOUND("Category not found with id: %s"),
    BOOK_NOT_FOUND("Book not found with id: %s"),
    CONFLICT("Available copies aren't available with this book id: %s"),
    CATEGORY_ALREADY_CREATED("Category already exists with name: %s"),
    BOOK_ALREADY_EXISTS("Book already exists with title and author: %s %s"),
    BORROW_ALREADY_EXISTS("You have already borrowed this book: %s"),
    BORROW_NOT_FOUND("Borrow not found with id: %s"),
    ISBN_ALREADY_USED("This ISBN (%s) is already used");

    private final String message;

}
