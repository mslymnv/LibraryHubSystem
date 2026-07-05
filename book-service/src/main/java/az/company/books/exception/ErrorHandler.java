package az.company.books.exception;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse handleException(NotFoundException exception) {
        return ErrorResponse.builder()
                .code(exception.getCode())
                .message(exception.getMessage())
                .build();
    }
    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception exception) {
        return ErrorResponse.builder()
                .code("Internal Error")
                .message(exception.getMessage())
                .build();
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleException(MethodArgumentNotValidException exception) {
        return ErrorResponse.builder()
                .code("Validation Error")
                .message(Objects.requireNonNull(exception.getBindingResult().getFieldError()).getDefaultMessage())
                .build();
    }
}
