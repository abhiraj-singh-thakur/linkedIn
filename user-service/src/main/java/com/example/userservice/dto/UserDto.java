package com.example.userservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDto {

    private Long id;
    private String name;
    private String password;
    private String email;
}
