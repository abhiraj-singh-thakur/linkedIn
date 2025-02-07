package com.example.postservice.events;


import lombok.Data;

@Data
public class PostCreatedEvent {
    private Long creatorId;
    private String content;
    private Long postId;
}
