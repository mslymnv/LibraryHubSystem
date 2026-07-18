package az.company.books.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.annotation.processing.Generated;

@Getter
@AllArgsConstructor
public class IsbnAlreadyUsedException extends RuntimeException {
    private String code;
    public IsbnAlreadyUsedException(String code,String message){
        super(message);
        this.code=code;
    }
}
