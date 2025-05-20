package ru.pin36bik.exceptions;

public class PresetNotFoundException extends RuntimeException {
    public PresetNotFoundException(final String message) {
        super(message);
    }
}
