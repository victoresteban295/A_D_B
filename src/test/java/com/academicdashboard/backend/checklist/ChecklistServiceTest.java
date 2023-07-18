package com.academicdashboard.backend.checklist;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.academicdashboard.backend.exception.ApiRequestException;
import com.academicdashboard.backend.user.UserRepository;

@Testcontainers
@DataMongoTest
public class ChecklistServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GrouplistRepository grouplistRepository;
    @Autowired
    private ChecklistRepository checklistRepository;
    @Autowired
    private CheckpointRepository checkpointRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    private TestData testData;
    private ChecklistService checklistService;

    @BeforeEach
    public void setUp() {
        this.checklistService = new ChecklistService(
                checklistRepository, 
                mongoTemplate);
        this.testData = new TestData(
                userRepository, 
                grouplistRepository, 
                checklistRepository, 
                checkpointRepository);
        testData.populateDatabase();
    }

    @AfterEach
    public void cleanup() {
        testData.cleanupDatabase();
    }

    @Test
    @DisplayName("Should Create a New Checklist Under User")
    public void shouldCreateNewChecklist() {
        //When
        Checklist expectedValue = checklistService
            .createChecklist("ju7db63uy678erdybncpo", "listTitle");
        String expectedValueId = expectedValue.getListId();

        //Then
        /* Assert that Newly Created Checklist Has Been Added As A Checklist Document*/
        Assertions.assertThat(checklistRepository
                .findChecklistByListId(expectedValueId)
                .get())
            .isEqualTo(expectedValue);

        /* Assert that Newly Created Checklist Has Been Added to User's Checklist Reference*/
        Assertions.assertThat(userRepository
                .findUserByUsername("testuser")
                .get()
                .getChecklists()
                .contains(expectedValue))
            .isEqualTo(true);
    }

    @Test
    @DisplayName("Should Modify an Existing Checklist")
    public void modifyExistingChecklistTitle() {
        //When
        Checklist expectedValue01 = checklistService.modifyChecklist("listIdA1", "newTitle01");
        Checklist expectedValue02 = checklistService.modifyChecklist("listIdA2", "newTitle02");
        Checklist expectedValue03 = checklistService.modifyChecklist("listIdB1", "newTitle03");
        Checklist expectedValue04 = checklistService.modifyChecklist("listIdB2", "newTitle04");
        Checklist expectedValue05 = checklistService.modifyChecklist("listIdC1", "newTitle05");
        Checklist expectedValue06 = checklistService.modifyChecklist("listIdD", "newTitle06");

        //Then
        Assertions.assertThat(checklistRepository.findChecklistByListId("listIdA1").get()).isEqualTo(expectedValue01);
        Assertions.assertThat(checklistRepository.findChecklistByListId("listIdA2").get()).isEqualTo(expectedValue02);
        Assertions.assertThat(checklistRepository.findChecklistByListId("listIdB1").get()).isEqualTo(expectedValue03);
        Assertions.assertThat(checklistRepository.findChecklistByListId("listIdB2").get()).isEqualTo(expectedValue04);
        Assertions.assertThat(checklistRepository.findChecklistByListId("listIdC1").get()).isEqualTo(expectedValue05);
        Assertions.assertThat(checklistRepository.findChecklistByListId("listIdD").get()).isEqualTo(expectedValue06);
    }

    @Test
    @DisplayName("Should Throw an ApiRequestException When Modifying Non-existent Checklist")
    public void throwExceptionModifyingNonexistentChecklist() {
        //When-Then
        Assertions.assertThatThrownBy(() -> {
            checklistService.modifyChecklist("XXXXX", "XXXXXX");
        }).isInstanceOf(ApiRequestException.class)
            .hasMessage("Checklist You Wanted to Modify Doesn't Exist");
    } 

    @Test
    @DisplayName("Should Delete Checklist with its Checkpoints")
    public void shouldDeleteChecklistWithCheckpoints() {
        //When
        checklistService.deleteChecklist("listIdA1");
        checklistService.deleteChecklist("listIdA2");
        checklistService.deleteChecklist("listIdB1");
        checklistService.deleteChecklist("listIdB2");
        checklistService.deleteChecklist("listIdC1");
        checklistService.deleteChecklist("listIdD");

        //Then
        Assertions.assertThat(checklistRepository.findAll().isEmpty()).isTrue();
        Assertions.assertThat(checkpointRepository.findAll().isEmpty()).isTrue();
        Assertions.assertThat(userRepository
                .findUserByUsername("testuser")
                .get()
                .getChecklists()
                .isEmpty())
            .isTrue();
        Assertions.assertThat(grouplistRepository
                .findGrouplistByGroupId("groupIdA")
                .get()
                .getChecklists()
                .isEmpty())
            .isTrue();
        Assertions.assertThat(grouplistRepository
                .findGrouplistByGroupId("groupIdB")
                .get()
                .getChecklists()
                .isEmpty())
            .isTrue();
    }

    @Test
    @DisplayName("Should Throw an ApiRequestException When Modifying Non-existent Checklist")
    public void throwExceptionDeletingNonexistentChecklist() {
        //When-Then
        Assertions.assertThatThrownBy(() -> {
            checklistService.deleteChecklist("XXXXX");
        }).isInstanceOf(ApiRequestException.class)
            .hasMessage("Checklist You Wanted to Delete Doesn't Exist");
    }
}
