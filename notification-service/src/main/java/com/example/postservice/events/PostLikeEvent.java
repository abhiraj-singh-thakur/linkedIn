package com.example.postservice.events;

import lombok.Data;

@Data
public class PostLikeEvent {
    private Long postId;
    private Long creatorId;
    private Long likedByUserId;
}
