package com.example.notificationservice.consumer;

import com.example.connectionservice.event.SendConnectionRequestEvent;
import com.example.notificationservice.service.SendNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConnectionServiceConsumer {


    private final SendNotification sendNotification;

    @KafkaListener(topics = "send-connection-request-topic")
    public void handleConnectionRequest(SendConnectionRequestEvent connectionRequestEvent) {
        String message = "You have received a connection request from user: " + connectionRequestEvent.getSenderId();
        sendNotification.sendNotification(connectionRequestEvent.getReceiverId(), message);
    }

    @KafkaListener(topics = "accept-connection-request-topic")
    public void handleAcceptConnectionRequest(SendConnectionRequestEvent connectionRequestEvent) {
        String message = "Your connection request has been accepted from user: " + connectionRequestEvent.getReceiverId();
        sendNotification.sendNotification(connectionRequestEvent.getSenderId(), message);
    }
}
