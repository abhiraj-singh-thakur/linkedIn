package com.example.postservice.service;

import com.example.postservice.auth.UserContextHolder;
import com.example.postservice.dto.PostCreateRequestDto;
import com.example.postservice.dto.PostDto;
import com.example.postservice.entity.Post;
import com.example.postservice.events.PostCreatedEvent;
import com.example.postservice.exception.EmptyBodyException;
import com.example.postservice.exception.ResourceNotFoundException;
import com.example.postservice.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private final KafkaTemplate<Long, PostCreatedEvent> kafkaTemplate;

    public PostDto createPost(PostCreateRequestDto postCreateRequestDto, Long userId) {
        if (postCreateRequestDto.getContent() == null || postCreateRequestDto.getContent().trim().equals("")) {
            throw new EmptyBodyException("Post content is empty");
        }
        Post post = modelMapper.map(postCreateRequestDto, Post.class);
        post.setUserId(userId);

        Post savedPost = postRepository.save(post);

        PostCreatedEvent postCreatedEvent = PostCreatedEvent.builder()
                .postId(savedPost.getId())
                .creatorId(userId)
                .content(savedPost.getContent())
                .build();

        kafkaTemplate.send("post-created-topic", postCreatedEvent);
        return modelMapper.map(savedPost, PostDto.class);
    }

    public PostDto getPostById(Long postId) {
        log.debug("Retrieving post by id: {}", postId);

        Long userId = UserContextHolder.getCurrentUserId();

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id " + postId));
        return modelMapper.map(post, PostDto.class);
    }

    public List<PostDto> getAllPostsOfUser(Long userId) {
        log.debug("Retrieving all posts of user: {}", userId);

        List<Post> posts = postRepository.findByUserId(userId);

        return posts.stream()
                .map(post -> modelMapper.map(post, PostDto.class))
                .collect(Collectors.toList());
    }


}
