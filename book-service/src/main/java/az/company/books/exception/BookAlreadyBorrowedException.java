package az.company.books.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class BookAlreadyBorrowedException extends RuntimeException {
    private String code;
    public BookAlreadyBorrowedException(String code, String message) {
        super(message);
        this.code = code;
    }
}
