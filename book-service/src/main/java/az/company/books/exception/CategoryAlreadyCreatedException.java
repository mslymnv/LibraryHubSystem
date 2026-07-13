package az.company.books.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryAlreadyCreatedException extends RuntimeException {
    private String code;
    public CategoryAlreadyCreatedException(String code, String message) {
        super(message);
        this.code = code;
    }
}
