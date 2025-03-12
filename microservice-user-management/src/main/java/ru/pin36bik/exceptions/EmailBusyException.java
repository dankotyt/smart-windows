package ru.pin36bik.exceptions;

public class EmailBusyException extends RuntimeException {
    public EmailBusyException(String message) {
        super(message);
    }
}
