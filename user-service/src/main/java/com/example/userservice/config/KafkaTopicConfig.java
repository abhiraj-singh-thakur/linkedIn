package com.example.userservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {
    @Bean
    public NewTopic CreatePersonRequestTopic() {
        return new NewTopic("create-person-request-topic", 3, (short) 1);
    }
}
