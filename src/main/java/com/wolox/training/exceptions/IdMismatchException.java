package com.wolox.training.exceptions;

public class IdMismatchException extends RuntimeException{
    public IdMismatchException() {
    }

    public IdMismatchException(String message) {
        super(message);
    }
}
