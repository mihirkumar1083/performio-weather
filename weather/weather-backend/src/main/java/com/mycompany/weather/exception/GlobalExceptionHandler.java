package com.mycompany.weather.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * GlobalExceptionHandler handles exceptions thrown in controllers globally.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles all exceptions and returns a proper error message with a 500 status.
     *
     * @param ex the exception to be handled.
     * @return a ResponseEntity containing the error message.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
