package az.company.users.exception;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Objects;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception exception) {
        return ErrorResponse
                .builder()
                .status(INTERNAL_SERVER_ERROR.value())
                .code("INTERNAL ERROR")
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        return ErrorResponse.builder()
                .status(BAD_REQUEST.value())
                .code("BAD_REQUEST")
                .message(Objects.requireNonNull(exception.getBindingResult().getFieldError()).getDefaultMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(UserPresentException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleUserPresentException(UserPresentException exception) {
        return ErrorResponse.builder()
                .status(BAD_REQUEST.value())
                .code(exception.getCode())
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException exception) {
        return ErrorResponse.builder()
                .status(NOT_FOUND.value())
                .code(exception.getCode())
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

}
