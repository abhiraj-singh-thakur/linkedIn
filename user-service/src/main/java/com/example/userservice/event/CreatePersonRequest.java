package com.example.userservice.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreatePersonRequest {
    private Long userId;
    private String name;
}
