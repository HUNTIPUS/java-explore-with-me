package ru.practicum.exceptions.exception;

public class DuplicateException extends RuntimeException {

    public DuplicateException(final String message) {
        super(message);
    }

    public String getReason() {
        return "Integrity constraint has been violated.";
    }
}
