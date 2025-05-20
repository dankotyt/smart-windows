package ru.pin36bik.exceptions;

public class NonUniqueWindowException extends RuntimeException {
    public NonUniqueWindowException(String message) {
        super(message);
    }
}
