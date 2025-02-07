package com.example.postservice.exception;

public class RuntimeConflictException extends RuntimeException {

    public RuntimeConflictException() {

    }

    public RuntimeConflictException(String message) {
        super(message);
    }
}
