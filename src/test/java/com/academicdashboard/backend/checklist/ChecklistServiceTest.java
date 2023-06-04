package com.academicdashboard.backend.checklist;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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
public class ChecklistServiceTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0.6");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private MongoTemplate mongoTemplate;

    @Mock
    private CheckpointService checkpointService;

    @Autowired
    private ChecklistRepository checklistRepository;

    private ChecklistService checklistService;

    @BeforeEach
    public void setUp() {
        this.checklistService = new ChecklistService(
                checkpointService, 
                checklistRepository, 
                mongoTemplate);
    }

    @AfterEach
    public void cleanup() {
        this.checklistRepository.deleteAll();
    }

    @Test
    @DisplayName("Should Modify an Existing Checklist")
    public void modifyExistingChecklistTitle() {
        //Given
        this.checklistRepository.insert(new Checklist("id01", "oldTitle"));
        
        //When
        checklistService.modifyChecklist("id01", "newTitle");

        //Then
        Checklist returnedValue = this.checklistRepository.findChecklistByListId("id01").get();
        Assertions.assertThat(returnedValue.getTitle()).isEqualTo("newTitle");
    }

    @Test
    @DisplayName("Should Throw an ApiRequestException When Modifying Non-existent Checklist")
    public void throwExceptionNonexistentChecklist() {
        //Given
        this.checklistRepository.insert(new Checklist("id01", "title01"));
        this.checklistRepository.insert(new Checklist("id02", "title02"));
        this.checklistRepository.insert(new Checklist("id03", "title03"));

        //Then
        Assertions.assertThatThrownBy(() -> {
            checklistService.modifyChecklist("id04", "title04");
        }).isInstanceOf(ApiRequestException.class)
            .hasMessage("Checklist You Wanted to Modify Doesn't Exist");
    } 
}
