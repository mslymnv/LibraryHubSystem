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
                .message("INTERNAL ERROR")
                .error(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        return ErrorResponse.builder()
                .status(BAD_REQUEST.value())
                .message("BAD REQUEST")
                .error(Objects.requireNonNull(exception.getBindingResult().getFieldError()).getDefaultMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(UserPresentException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleUserPresentException(UserPresentException exception) {
        return ErrorResponse.builder()
                .status(BAD_REQUEST.value())
                .message("BAD REQUEST")
                .error(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException exception) {
        return ErrorResponse.builder()
                .status(NOT_FOUND.value())
                .message("NOT FOUND")
                .error(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

}
