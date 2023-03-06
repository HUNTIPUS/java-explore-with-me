package ru.practicum.exceptions.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.exceptions.response.ErrorResponse;

import javax.validation.ValidationException;
import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class HandlerException {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> exc(ValidationException ex) {
        log.info("error code: 400");
        return new ResponseEntity<>(
                new ErrorResponse(HttpStatus.BAD_REQUEST,
                        "Incorrectly made request.",
                        ex.getMessage(),
                        LocalDateTime.now()),
                HttpStatus.BAD_REQUEST);
    }
}
