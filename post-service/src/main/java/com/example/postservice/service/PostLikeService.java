package com.example.postservice.service;


import com.example.postservice.auth.UserContextHolder;
import com.example.postservice.auth.UserInterceptor;
import com.example.postservice.entity.Post;
import com.example.postservice.entity.PostLike;
import com.example.postservice.events.PostLikeEvent;
import com.example.postservice.exception.BadRequestException;
import com.example.postservice.exception.ResourceNotFoundException;
import com.example.postservice.repository.PostLikeRepository;
import com.example.postservice.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final KafkaTemplate<Long, PostLikeEvent> kafkaTemplate;

    @Transactional
    public void LikePost(Long postId) {
        Long userId = UserContextHolder.getCurrentUserId();
        Post post = findPostByPostId(postId);
        if (postLikeRepository.existsByPostIdAndUserId(postId, userId)) {
            throw new BadRequestException("Post already liked!");
        } else {
            PostLike postLike = new PostLike();
            postLike.setPostId(postId);
            postLike.setUserId(userId);
            postLikeRepository.save(postLike);
            log.info("Liked the post: " + postId);

            post.setLikes(post.getLikes() + 1);

            PostLikeEvent postLikeEvent = PostLikeEvent.builder()
                    .postId(postId)
                    .likedByUserId(userId)
                    .creatorId(post.getUserId())
                    .build();
            kafkaTemplate.send("post-liked-topic", postId, postLikeEvent);
        }
    }

    @Transactional
    public void UnlikePost(Long postId) {
        Long userId = UserContextHolder.getCurrentUserId();
        log.info("Attempting to unlike the post: " + postId);
        Post post = findPostByPostId(postId);
        if (!postLikeRepository.existsByPostIdAndUserId(postId, userId))
            throw new BadRequestException("Post already disliked!");
        post.setLikes(post.getLikes() - 1);

        if (post.getLikes() > 0)
            post.setLikes(post.getLikes() - 1);
        else
            post.setLikes(0);
        postRepository.save(post);
        postLikeRepository.deleteByPostIdAndUserId(postId, userId);
        log.info("Unliked the post: " + postId);
    }

    public Post findPostByPostId(Long postId) {
        return postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));
    }
}
