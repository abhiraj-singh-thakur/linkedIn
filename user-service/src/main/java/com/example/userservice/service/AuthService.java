package com.example.userservice.service;


import com.example.userservice.dto.LoginRequestDto;
import com.example.userservice.dto.SignupRequestDto;
import com.example.userservice.dto.UserDto;
import com.example.userservice.entity.User;
import com.example.userservice.event.CreatePersonRequest;
import com.example.userservice.exception.BadRequestException;
import com.example.userservice.exception.ResourceNotFoundException;
import com.example.userservice.exception.RuntimeConflictException;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.utils.PasswordUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;
    private final KafkaTemplate<Long, CreatePersonRequest> createPersonKafkaTemplate;

    public UserDto signUp(SignupRequestDto signupRequestDto) {
        try {
            User user = modelMapper.map(signupRequestDto, User.class);
            if (userRepository.existsByEmail(signupRequestDto.getEmail()))
                throw new RuntimeConflictException("Email already registered");
            user.setPassword(PasswordUtil.hashPassword(signupRequestDto.getPassword()));
            User savedUser = userRepository.save(user);
            log.info("Saved user: {}", savedUser);
            CreatePersonRequest createPersonRequestEvent = CreatePersonRequest
                    .builder()
                    .userId(savedUser.getId())
                    .name(savedUser.getName())
                    .build();
            createPersonKafkaTemplate.send("create-person-request-topic", createPersonRequestEvent);
            log.info("Created person request send");
            return modelMapper.map(savedUser, UserDto.class);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public String login(LoginRequestDto loginRequestDto) {
        User user = userRepository
                .findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!PasswordUtil.checkPassword(loginRequestDto.getPassword(), user.getPassword()))
            throw new BadRequestException("Wrong password");

        return jwtService.generateAccessToken(user);
    }

}
