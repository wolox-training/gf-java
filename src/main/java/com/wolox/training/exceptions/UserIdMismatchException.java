package com.wolox.training.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "The User Id mismatch")
public class UserIdMismatchException extends Exception{
    public UserIdMismatchException() {
    }

    public UserIdMismatchException(String message) {
        super(message);
    }
}
