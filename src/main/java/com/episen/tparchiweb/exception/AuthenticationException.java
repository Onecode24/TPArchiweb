package com.episen.tparchiweb.exception;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException() {
        super("Invalid username or password");
    }

    public AuthenticationException(String message) {
        super(message);
    }
}
