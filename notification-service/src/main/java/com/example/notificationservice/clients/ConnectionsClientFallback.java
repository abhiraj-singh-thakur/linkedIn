package com.example.notificationservice.clients;

import com.example.notificationservice.dto.PersonDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ConnectionsClientFallback implements ConnectionsClient {
    @Override
    public List<PersonDTO> getFirstConnections(Long userId) {
        return new ArrayList<>();
    }
}