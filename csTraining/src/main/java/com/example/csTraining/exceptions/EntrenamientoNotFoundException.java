package com.example.csTraining.exceptions;

public class EntrenamientoNotFoundException extends RuntimeException {

    public EntrenamientoNotFoundException(String message) {
        super(message);
    }

    public EntrenamientoNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
