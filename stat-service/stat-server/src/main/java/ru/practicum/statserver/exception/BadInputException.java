package ru.practicum.statserver.exception;

public class BadInputException extends RuntimeException {
    public BadInputException() {
    }

    public BadInputException(String message) {
        super(message);
    }

    public BadInputException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadInputException(Throwable cause) {
        super(cause);
    }
}
