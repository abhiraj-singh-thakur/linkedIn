package com.example.connectionservice;

import com.example.connectionservice.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ConnectionServiceApplicationTests {

    @Autowired
    private PersonRepository personRepository;

    @Test
    void testNeo4jConnection() {
        assertThat(personRepository.count()).isGreaterThanOrEqualTo(0);
    }

}
