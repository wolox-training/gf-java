package com.wolox.training.exceptions;

/**
 * This class extends from RuntimeException and is used when an Object is not found
 *
 * @author Gabriel Fernandez
 * @version 1.0
 */
public class NotFoundException extends RuntimeException{
    public NotFoundException() {
    }

    public NotFoundException(String message) {
        super(message);
    }
}
