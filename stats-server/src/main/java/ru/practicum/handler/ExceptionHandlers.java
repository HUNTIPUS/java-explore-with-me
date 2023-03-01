package ru.practicum.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.response.ErrorResponse;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlers {

//    @ExceptionHandler
//    public ResponseEntity<ErrorResponse> exc(Throwable ex) {
//        log.info("Код ошибки: 500");
//        return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
//    }
}
