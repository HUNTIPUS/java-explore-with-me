package ru.practicum.exceptions.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.exceptions.exceptoin.ObjectExcistenceException;
import ru.practicum.exceptions.response.ErrorResponse;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class HandlerException {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> exc(ObjectExcistenceException ex) {
        log.info("error code: 404");
        return new ResponseEntity<>(
                new ErrorResponse(HttpStatus.NOT_FOUND,
                        ex.getReason(),
                        ex.getMessage(),
                        LocalDateTime.now()),
                HttpStatus.NOT_FOUND);
    }
}
