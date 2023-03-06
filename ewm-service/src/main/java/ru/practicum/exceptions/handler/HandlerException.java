package ru.practicum.exceptions.handler;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.exceptions.exceptoin.InvalidRequestException;
import ru.practicum.exceptions.exceptoin.ObjectExistenceException;
import ru.practicum.exceptions.response.ErrorResponse;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class HandlerException {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> exc(MethodArgumentNotValidException ex) {
        log.info("error code: 400");
        return new ResponseEntity<>(
                new ErrorResponse(HttpStatus.BAD_REQUEST,
                        "Incorrectly made request.",
                        String.format("Field: %s. Error: %s. Value: %s",
                                Objects.requireNonNull(ex.getFieldError()).getField(),
                                ex.getFieldError().getDefaultMessage(),
                                ex.getFieldError().getRejectedValue()),
                        LocalDateTime.now().withNano(0)),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> exc(Throwable ex) {
        log.info("error code: 400");
        return new ResponseEntity<>(
                new ErrorResponse(HttpStatus.BAD_REQUEST,
                        "Incorrectly made request.",
                        ex.getMessage(),
                        LocalDateTime.now().withNano(0)),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> exc(InvalidRequestException ex) {
        log.info("error code: 400");
        return new ResponseEntity<>(
                new ErrorResponse(HttpStatus.BAD_REQUEST,
                        ex.getReason(),
                        ex.getMessage(),
                        LocalDateTime.now().withNano(0)),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> exc(ObjectExistenceException ex) {
        log.info("error code: 404");
        return new ResponseEntity<>(
                new ErrorResponse(HttpStatus.NOT_FOUND,
                        ex.getReason(),
                        ex.getMessage(),
                        LocalDateTime.now().withNano(0)),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> exc(ConstraintViolationException ex) {
        log.info("error code: 409");
        return new ResponseEntity<>(
                new ErrorResponse(HttpStatus.CONFLICT,
                        "Integrity constraint has been violated.",
                        String.format("%s; SQL [%s]; constraint [%s]; nested exception is %s",
                                ex.getMessage(),
                                ex.getSQL(),
                                ex.getConstraintName(),
                                ex),
                        LocalDateTime.now().withNano(0)),
                HttpStatus.CONFLICT);
    }
}
