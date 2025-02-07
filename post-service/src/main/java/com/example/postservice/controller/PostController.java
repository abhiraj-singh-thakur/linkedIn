package com.example.postservice.controller;

import com.example.postservice.auth.UserContextHolder;
import com.example.postservice.dto.PersonDTO;
import com.example.postservice.dto.PostCreateRequestDto;
import com.example.postservice.dto.PostDto;
import com.example.postservice.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RefreshScope
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/core")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody PostCreateRequestDto postCreateRequestDto, HttpServletRequest request) {

        log.info("User-id: {}", request.getHeader("X-User-Id"));
        PostDto createdPost = postService.createPost(postCreateRequestDto, Long.parseLong(request.getHeader("X-User-Id")));
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long postId, HttpServletRequest request) {
        long userId = UserContextHolder.getCurrentUserId();
//        long userId = 7;
        log.info("User ID: {}", userId);
        PostDto postDto = postService.getPostById(postId);
        return ResponseEntity.ok(postDto);
    }

    @GetMapping("/users/{userId}/allPosts")
    public ResponseEntity<List<PostDto>> getPostByUserId(@PathVariable Long userId) {
        List<PostDto> posts = postService.getAllPostsOfUser(userId);
        return ResponseEntity.ok(posts);
    }

}
