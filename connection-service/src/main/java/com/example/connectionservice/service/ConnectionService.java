package com.example.connectionservice.service;

import com.example.connectionservice.dto.ConnectionRequestResponseDTO;
import com.example.connectionservice.dto.PersonDTO;
import com.example.connectionservice.entity.Person;
import com.example.connectionservice.auth.UserContextHolder;
import com.example.connectionservice.event.AcceptConnectionRequestEvent;
import com.example.connectionservice.event.SendConnectionRequestEvent;
import com.example.connectionservice.repository.PersonRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConnectionService {

    private final PersonRepository personRepository;
    private final KafkaTemplate<Long, SendConnectionRequestEvent> sendConnectionRequestKafkaTemplate;
    private final KafkaTemplate<Long, AcceptConnectionRequestEvent> acceptConnectionKafkaTemplate;

    @CircuitBreaker(name = "getConnections", fallbackMethod = "getFirstDegreeConnectionsFallback")
    @Retry(name = "getConnections")
    @Cacheable(cacheNames = "connections", key = "T(com.example.connectionservice.auth.UserContextHolder).getCurrentUserId()")
    public List<PersonDTO> getFirstDegreeConnections() {
        Long userId = UserContextHolder.getCurrentUserId();
        log.info("Getting first degree connections for userId: " + userId);
        List<Person> persons = personRepository.getFirstDegreeConnections(userId);
        log.info("Persons : " + persons);
        List<PersonDTO> personDTOs = new ArrayList<>();
        for (Person person : persons) {
            PersonDTO personDTO = new PersonDTO();
            personDTO.setUserId(person.getUserId());
            personDTO.setId(person.getId());
            personDTO.setName(person.getName());
            personDTOs.add(personDTO);
        }
        log.info("First degree connections: " + personDTOs);
        return personDTOs;
    }

    public void createPerson(Long userId, String name) {
        log.info("Creating the person with name: " + name);
        personRepository.createPerson(userId, name);
        log.info("Person with name: " + name + " has been created");
    }

    public ConnectionRequestResponseDTO sendConnectionRequest(Long receiverId) {
        Long senderId = UserContextHolder.getCurrentUserId();
        log.info("Sending connection request for senderId: " + senderId);

        if (senderId.equals(receiverId)) {
            throw new RuntimeException("SenderId and ReceiverId cannot be the same");
        }

        boolean alreadySentRequest = personRepository.connectionRequestExists(senderId, receiverId);
        if (alreadySentRequest) {
            throw new RuntimeException("Connection request already send");
        }
        boolean alreadyConnected = personRepository.alreadyConnected(senderId, receiverId);
        if (alreadyConnected) {
            throw new RuntimeException("You already connected");
        }
        log.info("Sending connection request for receiverId: " + receiverId);
        personRepository.addConnectionRequest(senderId, receiverId);

        SendConnectionRequestEvent sendConnectionRequestEvent = SendConnectionRequestEvent
                .builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .build();

        sendConnectionRequestKafkaTemplate.send("send-connection-request-topic", sendConnectionRequestEvent);
        log.info("Connection request sent");
        return ConnectionRequestResponseDTO.builder()
                .status("SUCCESS")
                .message("Connection request sent successfully")
                .senderId(senderId)
                .receiverId(receiverId)
                .build();
    }

    public ConnectionRequestResponseDTO acceptConnectionRequest(Long senderId) {
        Long receiverId = UserContextHolder.getCurrentUserId();

        if (!personRepository.connectionRequestExists(senderId, receiverId)) {
            throw new RuntimeException("No connection request exists for senderId: " + senderId);
        }

        personRepository.acceptConnectionRequest(senderId, receiverId);
        AcceptConnectionRequestEvent acceptConnectionRequestEvent = AcceptConnectionRequestEvent
                .builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .build();

        acceptConnectionKafkaTemplate.send("accept-connection-request-topic", acceptConnectionRequestEvent);


        log.info("Connection request accepted");
        return ConnectionRequestResponseDTO.builder()
                .status("SUCCESS")
                .message("Connection request accepted")
                .senderId(senderId)
                .receiverId(receiverId)
                .build();
    }

    public ConnectionRequestResponseDTO RejectConnectionRequest(Long senderId) {
        Long receiverId = UserContextHolder.getCurrentUserId();
        if (!personRepository.connectionRequestExists(senderId, receiverId)) {
            throw new RuntimeException("No connection request exists for senderId: " + senderId);
        }
        personRepository.rejectConnectionRequest(senderId, receiverId);
        log.info("Connection request rejected");
        return ConnectionRequestResponseDTO.builder()
                .status("SUCCESS")
                .message("Connection request rejected")
                .senderId(senderId)
                .receiverId(receiverId)
                .build();
    }

    public List<PersonDTO> getFirstDegreeConnectionsFallback(Exception e) {
        return new ArrayList<>();
    }
}
