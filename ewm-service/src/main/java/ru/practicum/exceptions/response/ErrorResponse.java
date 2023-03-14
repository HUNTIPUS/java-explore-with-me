package ru.practicum.exceptions.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private final HttpStatus status;
    private final String reason;
    private final String message;
    private final LocalDateTime timestamp;
}
