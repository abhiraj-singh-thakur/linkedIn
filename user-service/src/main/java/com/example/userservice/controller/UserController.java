package com.example.userservice.controller;

import com.example.userservice.dto.ProfileDTO;
import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RefreshScope
@RequestMapping("/core")
public class UserController {
    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<ProfileDTO> getMyProfile() {
        return new ResponseEntity<>(userService.getMyProfile(), HttpStatus.OK);
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<ProfileDTO> getProfile(@PathVariable Long id) {
        return new ResponseEntity<>(userService.getProfile(id), HttpStatus.OK);
    }
}
