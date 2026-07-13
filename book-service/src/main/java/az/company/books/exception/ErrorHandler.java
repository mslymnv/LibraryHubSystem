package az.company.books.exception;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static java.time.LocalDateTime.*;
import java.util.Objects;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse handleException(NotFoundException exception) {
        return ErrorResponse.builder()
                .status(NOT_FOUND.value())
                .message(exception.getCode())
                .error(exception.getMessage())
                .timestamp(now())
                .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception exception) {
        return ErrorResponse.builder()
                .status(INTERNAL_SERVER_ERROR.value())
                .message("Internal Server Error")
                .error(exception.getMessage())
                .timestamp(now())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleException(MethodArgumentNotValidException exception) {
        return ErrorResponse.builder()
                .status(BAD_REQUEST.value())
                .message("Bad Request")
                .error(Objects.requireNonNull(exception.getBindingResult().getFieldError()).getDefaultMessage())
                .timestamp(now())
                .build();
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(CONFLICT)
    public ErrorResponse handleException(ConflictException exception) {
        return ErrorResponse
                .builder()
                .status(CONFLICT.value())
                .message(exception.getCode())
                .error(exception.getMessage())
                .timestamp(now())
                .build();
    }

    @ExceptionHandler(CategoryAlreadyCreatedException.class)
    @ResponseStatus(CONFLICT)
    public ErrorResponse handleException(CategoryAlreadyCreatedException exception) {
        return ErrorResponse
                .builder()
                .status(CONFLICT.value())
                .message(exception.getCode())
                .error(exception.getMessage())
                .timestamp(now())
                .build();
    }

    @ExceptionHandler(BookAlreadyBorrowedException.class)
    @ResponseStatus(CONFLICT)
    public ErrorResponse handleException(BookAlreadyBorrowedException exception) {
        return ErrorResponse
                .builder()
                .status(CONFLICT.value())
                .message(exception.getCode())
                .error(exception.getMessage())
                .timestamp(now())
                .build();
    }

    @ExceptionHandler(BookAlreadyCreatedException.class)
    @ResponseStatus(CONFLICT)
    public ErrorResponse handleException(BookAlreadyCreatedException exception) {
        return ErrorResponse
                .builder()
                .status(CONFLICT.value())
                .message(exception.getCode())
                .error(exception.getMessage())
                .timestamp(now())
                .build();
    }
}
