package ru.pin36bik.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.pin36bik.exceptions.EmailBusyException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EmailBusyException.class)
    public ResponseEntity<ErrorResponse> handleEmailBusyException(EmailBusyException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
