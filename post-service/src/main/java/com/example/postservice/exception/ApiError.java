package com.example.postservice.exception;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@Builder
public class ApiError {

    private LocalDateTime timestamp;
    private String error;
    private HttpStatus statusCode;

    public ApiError() {
        this.timestamp = LocalDateTime.now();
    }

    public ApiError(String error, HttpStatus statusCode) {
        this();
        this.error = error;
        this.statusCode = statusCode;
    }

    public ApiError(LocalDateTime timestamp, String error, HttpStatus statusCode) {
        this.timestamp = timestamp;
        this.error = error;
        this.statusCode = statusCode;
    }
}
