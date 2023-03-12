package ru.practicum.exceptions.exception;

public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(final String message) {
        super(message);
    }

    public String getReason() {
        return "A request that was not made correctly.";
    }
}
