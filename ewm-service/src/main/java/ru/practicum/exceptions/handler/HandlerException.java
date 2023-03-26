package ru.practicum.exceptions.handler;

import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.exceptions.exception.*;
import ru.practicum.exceptions.response.ErrorResponse;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class HandlerException {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> exc(MethodArgumentNotValidException ex) {
        ResponseEntity<ErrorResponse> responseEntity = new ResponseEntity<>(
                new ErrorResponse(HttpStatus.BAD_REQUEST,
                        "Incorrectly made request.",
                        String.format("Field: %s. Error: %s. Value: %s",
                                Objects.requireNonNull(ex.getFieldError()).getField(),
                                ex.getFieldError().getDefaultMessage(),
                                ex.getFieldError().getRejectedValue()),
                        LocalDateTime.now().withNano(0)),
                HttpStatus.BAD_REQUEST);
        log.info(String.valueOf(responseEntity));
        return responseEntity;
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> exc(ValidationException ex) {
        ResponseEntity<ErrorResponse> responseEntity = new ResponseEntity<>(
                new ErrorResponse(HttpStatus.BAD_REQUEST,
                        "Incorrectly made request.",
                        ex.getMessage(),
                        LocalDateTime.now().withNano(0)),
                HttpStatus.BAD_REQUEST);
        log.info(String.valueOf(responseEntity));
        return responseEntity;
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> exc(MissingServletRequestParameterException ex) {
        ResponseEntity<ErrorResponse> responseEntity = new ResponseEntity<>(
                new ErrorResponse(HttpStatus.BAD_REQUEST,
                        "Incorrectly made request.",
                        ex.getMessage(),
                        LocalDateTime.now().withNano(0)),
                HttpStatus.BAD_REQUEST);
        log.info(String.valueOf(responseEntity));
        return responseEntity;
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> exc(ObjectExistenceException ex) {
        ResponseEntity<ErrorResponse> responseEntity = new ResponseEntity<>(
                new ErrorResponse(HttpStatus.NOT_FOUND,
                        ex.getReason(),
                        ex.getMessage(),
                        LocalDateTime.now().withNano(0)),
                HttpStatus.NOT_FOUND);
        log.info(String.valueOf(responseEntity));
        return responseEntity;
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> exc(InvalidRequestException ex) {
        ResponseEntity<ErrorResponse> responseEntity = new ResponseEntity<>(
                new ErrorResponse(HttpStatus.CONFLICT,
                        ex.getReason(),
                        ex.getMessage(),
                        LocalDateTime.now().withNano(0)),
                HttpStatus.CONFLICT);
        log.info(String.valueOf(responseEntity));
        return responseEntity;
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> exc(DuplicateException ex) {
        ResponseEntity<ErrorResponse> responseEntity = new ResponseEntity<>(
                new ErrorResponse(HttpStatus.CONFLICT,
                        ex.getReason(),
                        ex.getMessage(),
                        LocalDateTime.now().withNano(0)),
                HttpStatus.CONFLICT);
        log.info(String.valueOf(responseEntity));
        return responseEntity;
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> exc(TimeException ex) {
        ResponseEntity<ErrorResponse> responseEntity = new ResponseEntity<>(
                new ErrorResponse(HttpStatus.CONFLICT,
                        ex.getReason(),
                        ex.getMessage(),
                        LocalDateTime.now().withNano(0)),
                HttpStatus.CONFLICT);
        log.info(String.valueOf(responseEntity));
        return responseEntity;
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> exc(StatusException ex) {
        ResponseEntity<ErrorResponse> responseEntity = new ResponseEntity<>(
                new ErrorResponse(HttpStatus.CONFLICT,
                        ex.getReason(),
                        ex.getMessage(),
                        LocalDateTime.now().withNano(0)),
                HttpStatus.CONFLICT);
        log.info(String.valueOf(responseEntity));
        return responseEntity;
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> exc(ConstraintForeignKeyException ex) {
        ResponseEntity<ErrorResponse> responseEntity = new ResponseEntity<>(
                new ErrorResponse(HttpStatus.CONFLICT,
                        ex.getReason(),
                        ex.getMessage(),
                        LocalDateTime.now().withNano(0)),
                HttpStatus.CONFLICT);
        log.info(String.valueOf(responseEntity));
        return responseEntity;
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> exc(AccessException ex) {
        ResponseEntity<ErrorResponse> responseEntity = new ResponseEntity<>(
                new ErrorResponse(HttpStatus.CONFLICT,
                        ex.getReason(),
                        ex.getMessage(),
                        LocalDateTime.now().withNano(0)),
                HttpStatus.CONFLICT);
        log.info(String.valueOf(responseEntity));
        return responseEntity;
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> exc(ConstraintViolationException ex) {
        ResponseEntity<ErrorResponse> responseEntity = new ResponseEntity<>(
                new ErrorResponse(HttpStatus.CONFLICT,
                        "Integrity constraint has been violated.",
                        ex.getMessage(),
                        LocalDateTime.now().withNano(0)),
                HttpStatus.CONFLICT);
        log.info(String.valueOf(responseEntity));
        return responseEntity;
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> exc(PSQLException ex) {
        ResponseEntity<ErrorResponse> responseEntity = new ResponseEntity<>(
                new ErrorResponse(HttpStatus.CONFLICT,
                        "Integrity constraint has been violated.",
                        ex.getMessage(),
                        LocalDateTime.now().withNano(0)),
                HttpStatus.CONFLICT);
        log.info(String.valueOf(responseEntity));
        return responseEntity;
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> exc(HttpMessageNotReadableException ex) {
        ResponseEntity<ErrorResponse> responseEntity = new ResponseEntity<>(
                new ErrorResponse(HttpStatus.CONFLICT,
                        "Integrity constraint has been violated.",
                        ex.getMessage(),
                        LocalDateTime.now().withNano(0)),
                HttpStatus.CONFLICT);
        log.info(String.valueOf(responseEntity));
        return responseEntity;
    }

}
