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
public class GrouplistRepositoryTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0.6");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private GrouplistRepository grouplistRepository;

    @AfterEach
    public void cleanup() {
        this.grouplistRepository.deleteAll();
    }

    @Test
    @DisplayName("Should Find a Grouplist Document Using it's groupId")
    public void shouldFindGrouplistByGrouId() {
        //Given 
        Grouplist expectedValue = new Grouplist("id001", "Grouplist Title01");
        this.grouplistRepository.insert(expectedValue);
        this.grouplistRepository.insert(new Grouplist("id002", "Grouplist Title02"));
        this.grouplistRepository.insert(new Grouplist("id003", "Grouplist Title03"));

        //When
        Grouplist returnedValue = grouplistRepository.findGrouplistByGroupId(expectedValue.getGroupId()).get();

        //Then
        Assertions.assertThat(returnedValue.getTitle()).isEqualTo(expectedValue.getTitle());
    }
}
