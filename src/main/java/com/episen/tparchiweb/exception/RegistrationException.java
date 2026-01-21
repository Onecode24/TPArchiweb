package com.episen.tparchiweb.exception;

public class RegistrationException extends RuntimeException {
    public RegistrationException() {
        super("An error occurred during registration");
    }

    public RegistrationException(String message) {
        super(message);
    }
}
