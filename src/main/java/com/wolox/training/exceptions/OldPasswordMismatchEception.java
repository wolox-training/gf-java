package com.wolox.training.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "The Old Password mismatch")
public class OldPasswordMismatchEception extends Exception{
    public OldPasswordMismatchEception() {
    }

    public OldPasswordMismatchEception(String message) {
        super(message);
    }
}
