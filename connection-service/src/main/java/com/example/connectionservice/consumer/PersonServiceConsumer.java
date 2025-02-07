package com.example.connectionservice.consumer;

import com.example.connectionservice.service.ConnectionService;
import com.example.userservice.event.CreatePersonRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PersonServiceConsumer {

    private final ConnectionService connectionService;

    @KafkaListener(topics = "create-person-request-topic")
    public void createPerson(CreatePersonRequest createPersonRequestEvent) {
        log.info("Create Person Request: {}", createPersonRequestEvent);
        connectionService.createPerson(createPersonRequestEvent.getUserId(), createPersonRequestEvent.getName());
    }
}
