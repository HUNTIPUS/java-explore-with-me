package ru.practicum.exceptions.exception;

public class StatusException extends RuntimeException {

    public StatusException(final String message) {
        super(message);
    }

    public String getReason() {
        return "Integrity constraint has been violated.";
    }
}
