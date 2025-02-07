package com.example.postservice.controller;

import com.example.postservice.service.PostLikeService;
import com.example.postservice.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RefreshScope
@RequiredArgsConstructor
@RequestMapping("/likes")
public class LikeController {

    private final PostService postService;
    private final PostLikeService postLikeService;

    @PostMapping("/{postId}")
    public ResponseEntity<Void> likePost(@PathVariable Long postId) {
        postLikeService.LikePost(postId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> unlikePost(@PathVariable Long postId) {
        postLikeService.UnlikePost(postId);
        return ResponseEntity.noContent().build();
    }
}
