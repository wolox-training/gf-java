package com.wolox.training.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This class extends from RuntimeException and is used when an Object is not found
 *
 * @author Gabriel Fernandez
 * @version 1.0
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "The Book is not found")
public class BookNotFoundException extends Exception{
    public BookNotFoundException() {
    }

    public BookNotFoundException(String message) {
        super(message);
    }
}
