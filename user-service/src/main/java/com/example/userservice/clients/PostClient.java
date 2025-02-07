package com.example.userservice.clients;

import com.example.userservice.dto.PostDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "post-service", path = "/posts")
public interface PostClient {

    @GetMapping("core/users/{userId}/allPosts")
    List<PostDTO> getPostsByUserId(@PathVariable Long userId);
}
