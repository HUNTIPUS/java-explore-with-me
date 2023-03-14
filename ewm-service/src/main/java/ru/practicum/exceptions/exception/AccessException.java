package ru.practicum.exceptions.exception;

public class AccessException extends RuntimeException {

    public AccessException(final String message) {
        super(message);
    }

    public String getReason() {
        return "Integrity constraint has been violated.";
    }
}
