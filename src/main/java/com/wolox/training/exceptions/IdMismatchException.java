package com.wolox.training.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This class extends from RuntimeException and is used when two Id are not the same
 *
 * @author Gabriel Fernandez
 * @version 1.0
 */
@ResponseStatus(code = HttpStatus.CONFLICT, reason = "The Object Id mismatch")
public class IdMismatchException extends Exception{
    public IdMismatchException() {
    }

    public IdMismatchException(String message) {
        super(message);
    }
}
