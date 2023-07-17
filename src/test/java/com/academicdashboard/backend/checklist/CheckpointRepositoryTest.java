package com.academicdashboard.backend.checklist;

import java.util.ArrayList;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


@Disabled
@Testcontainers //Register Testcontainer
@DataMongoTest
@EnableMongoRepositories
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
        Checkpoint expectedValue = Checkpoint.builder()
            .pointId("id001")
            .content("Content01")
            .isComplete(false)
            .isSubpoint(false)
            .subCheckpoints(new ArrayList<>())
            .build();
        this.checkpointRepository.insert(expectedValue);
        this.checkpointRepository.insert(
                Checkpoint.builder()
                .pointId("id002")
                .content("Content02")
                .isComplete(false)
                .isSubpoint(false)
                .subCheckpoints(new ArrayList<>())
                .build()
                );
        this.checkpointRepository.insert(
                Checkpoint.builder()
                .pointId("id003")
                .content("Content03")
                .isComplete(false)
                .isSubpoint(false)
                .subCheckpoints(new ArrayList<>())
                .build()
                );

        //When
        Checkpoint returnedValue = checkpointRepository.findCheckpointByPointId("id001").get();

        //Then
        Assertions.assertThat(returnedValue.getContent()).isEqualTo(expectedValue.getContent());
    }
}
