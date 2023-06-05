package com.academicdashboard.backend.checklist;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.academicdashboard.backend.exception.ApiRequestException;

@Testcontainers
@DataMongoTest
public class CheckpointServiceTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0.6");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private CheckpointRepository checkpointRepository;

    private CheckpointService checkpointService;

    @BeforeEach
    public void setUp() {
        this.checkpointService = new CheckpointService(
                checkpointRepository, 
                mongoTemplate);
    }

    @AfterEach
    public void cleanup() {
        this.checkpointRepository.deleteAll();
    }

    @Test
    @DisplayName("Should Delete Checkpoint")
    public void shouldDeleteCheckpoint() {
        //Given
        checkpointRepository.insert(new Checkpoint("12345", "content", false, false));

        //When 
        checkpointService.deleteCheckpoint("12345");

        //Then
        Assertions.assertThat(checkpointRepository.findAll().isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Should Throw a ApiRequestException When Deleteing Non-existent Checkpoint")
    public void throwExceptionNonexistentCheckpoint() {
        //Given
        checkpointRepository.insert(new Checkpoint("12345", "content", false, false));

        //Then
        Assertions.assertThatThrownBy(() -> {
            checkpointService.deleteCheckpoint("09876");
        }).isInstanceOf(ApiRequestException.class)
            .hasMessage("Checkpoint You Wanted to Delete Doesn't Exist");
    }
}
