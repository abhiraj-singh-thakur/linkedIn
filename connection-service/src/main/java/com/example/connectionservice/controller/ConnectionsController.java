package com.example.connectionservice.controller;

import com.example.connectionservice.auth.UserContextHolder;
import com.example.connectionservice.dto.ConnectionRequestResponseDTO;
import com.example.connectionservice.dto.PersonDTO;
import com.example.connectionservice.entity.Person;
import com.example.connectionservice.service.ConnectionService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/core")
@RequiredArgsConstructor
@RefreshScope
public class ConnectionsController {

    private final ConnectionService connectionService;

    @RateLimiter(name = "connectionRequest")
    @GetMapping("/first-connections")
    public ResponseEntity<List<PersonDTO>> getFirstConnections() {
        return ResponseEntity.ok(connectionService.getFirstDegreeConnections());
    }
    
    @RateLimiter(name = "connectionRequest")
    @PostMapping("/request/{userId}")
    public ResponseEntity<ConnectionRequestResponseDTO> requestConnection(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(connectionService.sendConnectionRequest(userId));
    }

    @PostMapping("/accept/{userId}")
    public ResponseEntity<ConnectionRequestResponseDTO> acceptConnectionRequest(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(connectionService.acceptConnectionRequest(userId));
    }

    @PostMapping("/reject/{userId}")
    public ResponseEntity<ConnectionRequestResponseDTO> rejectConnectionRequest(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(connectionService.RejectConnectionRequest(userId));
    }
}
