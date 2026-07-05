package az.company.books.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotFoundException extends RuntimeException {
    private String code;

    public NotFoundException( String code,String message) {
        super(message);
        this.code = code;
    }
}
