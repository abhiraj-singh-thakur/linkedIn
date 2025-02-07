package com.example.notificationservice.clients;


import com.example.notificationservice.dto.PersonDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "connection-service", path = "/connections", fallback = ConnectionsClientFallback.class)
public interface ConnectionsClient {

    @CircuitBreaker(name = "connectionsService")
    @RateLimiter(name = "connectionsService")
    @Retry(name = "connectionsService")
    @GetMapping("/core/first-connections")
    List<PersonDTO> getFirstConnections(@RequestHeader("X-User-Id") Long userId);

}
