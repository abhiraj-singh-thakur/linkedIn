package com.example.notificationservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationDTO {
    private String message;
    private String createdAt;
}