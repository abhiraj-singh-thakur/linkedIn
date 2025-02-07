package com.example.postservice.exception;

public class EmptyBodyException extends RuntimeException {
    public EmptyBodyException() {
    }

    public EmptyBodyException(String message) {
        super(message);
    }
}
