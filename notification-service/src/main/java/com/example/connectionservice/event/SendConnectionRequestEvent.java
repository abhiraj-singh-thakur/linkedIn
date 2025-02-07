package com.example.connectionservice.event;


import lombok.Data;

@Data
public class SendConnectionRequestEvent {
    private Long senderId;
    private Long receiverId;
}
