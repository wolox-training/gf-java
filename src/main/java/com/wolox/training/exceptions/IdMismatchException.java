package com.wolox.training.exceptions;

/**
 * This class extends from RuntimeException and is used when two Id are not the same
 *
 * @author Gabriel Fernandez
 * @version 1.0
 */
public class IdMismatchException extends RuntimeException{
    public IdMismatchException() {
    }

    public IdMismatchException(String message) {
        super(message);
    }
}
