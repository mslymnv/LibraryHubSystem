package az.company.users.exception;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception exception) {
        return ErrorResponse
                .builder()
                .code("INTERNAL ERROR")
                .message(exception.getMessage())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        return ErrorResponse.builder()
                .code("BAD_REQUEST")
                .message(Objects.requireNonNull(exception.getBindingResult().getFieldError()).getDefaultMessage())
                .build();
    }

    @ExceptionHandler(UserPresentException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleUserPresentException(UserPresentException exception) {
        return ErrorResponse.builder()
                .code(exception.getCode())
                .message(exception.getMessage())
                .build();
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException exception) {
        return ErrorResponse.builder()
                .code(exception.getCode())
                .message(exception.getMessage())
                .build();
    }

}
