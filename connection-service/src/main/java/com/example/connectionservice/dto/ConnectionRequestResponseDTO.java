package com.example.connectionservice.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConnectionRequestResponseDTO {
    private String status;
    private String message;
    private Long senderId;
    private Long receiverId;
}
