package ru.pin36bik.exceptions;

public class InvalidWindowIdException extends RuntimeException {
    public InvalidWindowIdException(String message) {
        super(message);
    }
}
