package com.wolox.training.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * This class is a handler for the custom exceptions
 *
 * @author Gabriel Fernandez
 * @version 1.0
 */
@ControllerAdvice
public class RestExceptionHandler {

    /**
     * This method handles NotFoundExceptions retrieving an httpStatus 404 NOT FOUND
     * @param e: Exception previously caught
     * @return HttpStatus 404 NOT FOUND
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity notFoundException (Exception e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    /**
     * This method handles IdMismatchExceptions retrieving an httpStatus 409 CONFLICT
     * @param e: Exception previously caught
     * @return HttpStatus 409 CONFLICT
     */
    @ExceptionHandler(IdMismatchException.class)
    public ResponseEntity idMismatchException (Exception e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
}
