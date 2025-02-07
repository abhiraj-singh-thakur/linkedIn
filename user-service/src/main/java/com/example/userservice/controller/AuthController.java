package com.example.userservice.controller;

import com.example.userservice.dto.LoginRequestDto;
import com.example.userservice.dto.ProfileDTO;
import com.example.userservice.dto.SignupRequestDto;
import com.example.userservice.dto.UserDto;
import com.example.userservice.service.AuthService;
import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RefreshScope
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@RequestBody SignupRequestDto signupRequestDto) {
        UserDto user = authService.signUp(signupRequestDto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequestDto) {
        String token = authService.login(loginRequestDto);
        return new ResponseEntity<>("token: " + token, HttpStatus.OK);
    }
}
