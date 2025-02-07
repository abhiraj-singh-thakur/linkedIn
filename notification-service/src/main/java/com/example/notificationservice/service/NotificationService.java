package com.example.notificationservice.service;

import com.example.notificationservice.dto.NotificationDTO;
import com.example.notificationservice.entity.Notification;
import com.example.notificationservice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    final private NotificationRepository notificationRepository;
    final private ModelMapper modelMapper;

    public List<NotificationDTO> getNotifications(Long userId) {
        log.info("Fetching notifications for userId: {}", userId);
        List<Notification> notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
        log.info("Notifications: {}", notifications);

        List<NotificationDTO> notificationDTOS = notifications.stream()
                .map(notification -> {
                    NotificationDTO dto = modelMapper.map(notification, NotificationDTO.class);
                    dto.setCreatedAt(notification.getCreatedAt()
                            .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
                    return dto;
                })
                .collect(Collectors.toList());

        log.info("Notification: {}", notificationDTOS);
        return notificationDTOS;
    }
}
