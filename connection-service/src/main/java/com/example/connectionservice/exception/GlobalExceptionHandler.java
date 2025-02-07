package com.example.connectionservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequestException(BadRequestException e) {
        ApiError apiError = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .error(e.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST)
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> handleRuntimeException(RuntimeException e) {
        ApiError apiError = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .error(e.getMessage())
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
