package az.company.books.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookAlreadyCreatedException extends RuntimeException {
    private String code;
    public BookAlreadyCreatedException(String code, String message) {
        super(message);
        this.code = code;
    }
}
