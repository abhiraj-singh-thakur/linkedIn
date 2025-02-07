package com.example.userservice.exception;

public class RuntimeConflictException extends RuntimeException {

    public RuntimeConflictException() {

    }

    public RuntimeConflictException(String message) {
        super(message);
    }
}
