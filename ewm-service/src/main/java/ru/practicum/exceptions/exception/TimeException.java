package ru.practicum.exceptions.exception;

public class TimeException extends RuntimeException {
    public TimeException(final String message) {
        super(message);
    }

    public String getReason() {
        return "Integrity constraint has been violated.";
    }
}
