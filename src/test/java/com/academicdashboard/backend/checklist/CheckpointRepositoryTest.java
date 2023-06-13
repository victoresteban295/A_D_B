package com.academicdashboard.backend.checklist;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers //Register Testcontainer
@DataMongoTest
public class CheckpointRepositoryTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0.6");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private CheckpointRepository checkpointRepository;

    @AfterEach
    public void cleanup() {
        this.checkpointRepository.deleteAll();
    }

    @Test
    @DisplayName("Should Find a Checkpoint Document Using it's pointId")
    public void shouldFindCheckpointByPointId() {
        //Given
        Checkpoint expectedValue = new Checkpoint("id001", "Content01", false, false);
        this.checkpointRepository.insert(expectedValue);
        this.checkpointRepository.insert(new Checkpoint("id002", "Content02", false, false));
        this.checkpointRepository.insert(new Checkpoint("id003", "Content03", false, false));

        //When
        Checkpoint returnedValue = checkpointRepository.findCheckpointByPointId("id001").get();

        //Then
        Assertions.assertThat(returnedValue.getContent()).isEqualTo(expectedValue.getContent());
    }
}
