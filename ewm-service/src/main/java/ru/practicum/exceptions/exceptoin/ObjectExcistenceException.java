package ru.practicum.exceptions.exceptoin;

public class ObjectExcistenceException extends RuntimeException{
    public ObjectExcistenceException(final String message) {
        super(message);
    }

    public String getReason() {
        return "The required object was not found.";
    }
}
