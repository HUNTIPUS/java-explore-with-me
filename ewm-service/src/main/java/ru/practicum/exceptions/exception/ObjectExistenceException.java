package ru.practicum.exceptions.exception;

public class ObjectExistenceException extends RuntimeException {
    public ObjectExistenceException(final String message) {
        super(message);
    }

    public String getReason() {
        return "The required object was not found.";
    }
}
