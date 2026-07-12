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
                .status(NOT_FOUND.value())
                .message("Not Found")
                .error(exception.getMessage())
                .build();
    }
    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception exception) {
        return ErrorResponse.builder()
                .status(INTERNAL_SERVER_ERROR.value())
                .message("Internal Server Error")
                .error(exception.getMessage())
                .build();
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleException(MethodArgumentNotValidException exception) {
        return ErrorResponse.builder()
                .status(BAD_REQUEST.value())
                .message("Bad Request")
                .error(Objects.requireNonNull(exception.getBindingResult().getFieldError()).getDefaultMessage())
                .build();
    }
    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(CONFLICT)
    public ErrorResponse handleException(ConflictException exception) {
        return ErrorResponse
                .builder()
                .status(CONFLICT.value())
                .message("Conflict")
                .error(exception.getMessage())
                .build();
    }
}
