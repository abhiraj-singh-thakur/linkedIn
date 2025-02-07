package com.example.notificationservice.consumer;

import com.example.notificationservice.dto.PersonDTO;
import com.example.notificationservice.clients.ConnectionsClient;
import com.example.notificationservice.service.SendNotification;
import com.example.postservice.events.PostCreatedEvent;
import com.example.postservice.events.PostLikeEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostsServiceConsumer {

    private final ConnectionsClient connectionsClient;
    private final SendNotification sendNotification;

    @KafkaListener(topics = "post-created-topic")
    public void handlePostCreated(PostCreatedEvent postCreatedEvent) {
        List<PersonDTO> connections = connectionsClient.getFirstConnections(postCreatedEvent.getCreatorId());

        log.info("Sending notifications: handlePostCreated: {}", postCreatedEvent);


        for (PersonDTO connection : connections) {
            sendNotification.sendNotification(connection.getUserId(), "Your connection " + postCreatedEvent.getCreatorId() + " has created a post, Check it out");
        }
    }

    @KafkaListener(topics = "post-liked-topic")
    public void handlePostLiked(PostLikeEvent postLikeEvent) {

//        log.info("Sending notifications: handlePostLiked: {}", postLikeEvent);
        String message = String.format("Your post, %d has been liked by %d", postLikeEvent.getPostId(), postLikeEvent.getLikedByUserId());

        sendNotification.sendNotification(postLikeEvent.getCreatorId(), message);
    }


}
