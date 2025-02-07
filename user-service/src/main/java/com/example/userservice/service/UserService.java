package com.example.userservice.service;

import com.example.userservice.auth.UserContextHolder;
import com.example.userservice.clients.PostClient;
import com.example.userservice.dto.PostDTO;
import com.example.userservice.dto.ProfileDTO;
import com.example.userservice.entity.User;
import com.example.userservice.exception.ResourceNotFoundException;
import com.example.userservice.repository.UserRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PostClient postServiceClient;

    @CircuitBreaker(name = "postCircuitBreaker", fallbackMethod = "getMyProfileFallback")
    @RateLimiter(name = "postRateLimiter")
    @Retry(name = "postRetry")
    @Cacheable(cacheNames = "my-profile", key = "T(com.example.userservice.auth.UserContextHolder).getCurrentUserId()")
    public ProfileDTO getMyProfile() {
        Long userId = UserContextHolder.getCurrentUserId();

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        ProfileDTO profileDTO = modelMapper.map(user, ProfileDTO.class);
        List<PostDTO> userPosts = postServiceClient.getPostsByUserId(userId);
        profileDTO.setPosts(userPosts);

        return profileDTO;
    }

    @CircuitBreaker(name = "postCircuitBreaker", fallbackMethod = "getUserProfileFallback")
    @RateLimiter(name = "postRateLimiter")
    @Retry(name = "postRetry")
    @Cacheable(cacheNames = "other-profile", key = "#userId")
    public ProfileDTO getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        ProfileDTO profileDTO = modelMapper.map(user, ProfileDTO.class);
        List<PostDTO> userPosts = postServiceClient.getPostsByUserId(userId);
        profileDTO.setPosts(userPosts);

        return profileDTO;
    }

    public ProfileDTO getMyProfileFallback(Exception ex) {
        log.error("Fallback executed for current user with error: {}", ex.getMessage());
        return ProfileDTO.builder()
                .name("N/A")
                .email("N/A")
                .posts(Collections.emptyList())
                .build();
    }

    public ProfileDTO getUserProfileFallback(Long userId, Exception ex) {
        log.error("Fallback executed for userId: {} with error: {}", userId, ex.getMessage());
        return ProfileDTO.builder()
                .name("N/A")
                .email("N/A")
                .posts(Collections.emptyList())
                .build();
    }
}
