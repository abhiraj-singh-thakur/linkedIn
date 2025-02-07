package com.example.userservice.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class PostDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String content;
    private Long userId;
    private LocalDateTime createdAt;
    private Integer likes;
}
