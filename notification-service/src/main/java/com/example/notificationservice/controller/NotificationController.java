package com.example.notificationservice.controller;

import com.example.notificationservice.auth.UserContextHolder;
import com.example.notificationservice.service.NotificationService;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/core")
@RefreshScope
public class NotificationController {

    final private NotificationService notificationService;

    @GetMapping("/getNotification")
    public ResponseEntity<?> getNotification() {
        return ResponseEntity.ok(notificationService.getNotifications(UserContextHolder.getCurrentUserId()));
    }

}
