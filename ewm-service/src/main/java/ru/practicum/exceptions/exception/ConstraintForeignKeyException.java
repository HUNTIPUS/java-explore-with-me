package ru.practicum.exceptions.exception;

public class ConstraintForeignKeyException extends RuntimeException {
    public ConstraintForeignKeyException(final String message) {
        super(message);
    }

    public String getReason() {
        return "For the requested operation the conditions are not met.";
    }
}
