package com.example.csTraining.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomAuthenticationException extends RuntimeException {
    private final HttpStatus status;

    public CustomAuthenticationException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}

