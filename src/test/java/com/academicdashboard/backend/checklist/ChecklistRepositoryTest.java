package com.academicdashboard.backend.checklist;

import java.util.List;

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
public class ChecklistRepositoryTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0.6");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        //Override the "spring.data.mongodb.uri" to point to the local database container
        //The Testcontainer exposes a random ephemeral port)
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private ChecklistRepository checklistRepository;

    @AfterEach
    public void cleanup() {
        this.checklistRepository.deleteAll();
    }

    @Test
    @DisplayName("Should Insert Checklist to Repository")
    public void canInsertChecklistToRepository() {
        Checklist expectedValue = new Checklist("id01", "title01");
        this.checklistRepository.insert(expectedValue);
        List<Checklist> checklists = checklistRepository.findAll();
        Assertions.assertThat(checklists.contains(expectedValue)).isTrue();  
    }

    @Test
    @DisplayName("Should Find Checklist Using ListId")
    public void canFindChecklistByListId() {
        Checklist expectedValue = new Checklist("id01", "title01");
        this.checklistRepository.insert(expectedValue);
        Checklist returnedValue = checklistRepository.findChecklistByListId("id01").get(); 
        Assertions.assertThat(returnedValue.getTitle()).isEqualTo(expectedValue.getTitle());
    }
}
