package com.wolox.training.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.ALREADY_REPORTED, reason = "The Book is Already Owned")
public class BookAlreadyOwnedException extends Exception{
    public BookAlreadyOwnedException() {
    }

    public BookAlreadyOwnedException(String message) {
        super(message);
    }
}
