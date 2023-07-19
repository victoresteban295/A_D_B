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
public class CheckpointServiceTest {

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
    private CheckpointService checkpointService;

    @BeforeEach
    public void setUp() {
        this.checkpointService = new CheckpointService(
                checkpointRepository, 
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
    @DisplayName("Should Create a New Checkpoint Under an Existing Checklist")
    public void shouldCreateNewCheckpointUnderExistingChecklist() {
        //When
        Checklist checklist01 = checkpointService.addCheckpoint("listIdA1", "New Checkpoint Under listIdA1");
        int size01 = checklist01.getCheckpoints().size() - 1;
        Checkpoint expectedValue01 = checklist01.getCheckpoints().get(size01);

        Checklist checklist02 = checkpointService.addCheckpoint("listIdA2", "New Checkpoint Under listIdA2");
        int size02 = checklist02.getCheckpoints().size() - 1;
        Checkpoint expectedValue02 = checklist02.getCheckpoints().get(size02);

        Checklist checklist03 = checkpointService.addCheckpoint("listIdB1", "New Checkpoint Under listIdB1");
        int size03 = checklist03.getCheckpoints().size() - 1;
        Checkpoint expectedValue03 = checklist03.getCheckpoints().get(size03);

        Checklist checklist04 = checkpointService.addCheckpoint("listIdB2", "New Checkpoint Under listIdB2");
        int size04 = checklist04.getCheckpoints().size() - 1;
        Checkpoint expectedValue04 = checklist04.getCheckpoints().get(size04);

        Checklist checklist05 = checkpointService.addCheckpoint("listIdC1", "New Checkpoint Under listIdC1");
        int size05 = checklist05.getCheckpoints().size() - 1;
        Checkpoint expectedValue05 = checklist05.getCheckpoints().get(size05);

        Checklist checklist06 = checkpointService.addCheckpoint("listIdD", "New Checkpoint Under listIdD");
        int size06 = checklist06.getCheckpoints().size() - 1;
        Checkpoint expectedValue06 = checklist06.getCheckpoints().get(size06);

        //Then
        Assertions.assertThat(checkpointRepository
                .findCheckpointByPointId(expectedValue01.getPointId())
                .get())
            .isEqualTo(expectedValue01);
        Assertions.assertThat(checkpointRepository
                .findCheckpointByPointId(expectedValue02.getPointId())
                .get())
            .isEqualTo(expectedValue02);
        Assertions.assertThat(checkpointRepository
                .findCheckpointByPointId(expectedValue03.getPointId())
                .get())
            .isEqualTo(expectedValue03);
        Assertions.assertThat(checkpointRepository
                .findCheckpointByPointId(expectedValue04.getPointId())
                .get())
            .isEqualTo(expectedValue04);
        Assertions.assertThat(checkpointRepository
                .findCheckpointByPointId(expectedValue05.getPointId())
                .get())
            .isEqualTo(expectedValue05);
        Assertions.assertThat(checkpointRepository
                .findCheckpointByPointId(expectedValue06.getPointId())
                .get())
            .isEqualTo(expectedValue06);
    }

    @Test
    @DisplayName("Should Throw an ApiRequestException When Creating a New Checkpoint Under a Non-Existent Checklist")
    public void throwExceptionCreatingNewCheckpointUnderNonExistentChecklist() {
        //When-Then
        Assertions.assertThatThrownBy(() -> {
            checkpointService.addCheckpoint("XXXXX", "Checkpoint Content");
        }).isInstanceOf(ApiRequestException.class)
            .hasMessage("Checklist You Provided Doesn't Exist");
    }

    @Test
    @DisplayName("Should Modify an Existing Checkpoint")
    public void shouldModifyExistingCheckpoint() {
        //When
        //Subcheckpoints
        Checkpoint expectedValue13 = checkpointService.modifyCheckpoint("pointIdA11A", "New ContentA11A");
        Checkpoint expectedValue14 = checkpointService.modifyCheckpoint("pointIdA11B", "New ContentA11B");
        Checkpoint expectedValue15 = checkpointService.modifyCheckpoint("pointIdB11A", "New ContentB11A");
        Checkpoint expectedValue16 = checkpointService.modifyCheckpoint("pointIdB11B", "New ContentB11B");
        Checkpoint expectedValue17 = checkpointService.modifyCheckpoint("pointIdC11A", "New ContentC11A");
        Checkpoint expectedValue18 = checkpointService.modifyCheckpoint("pointIdC11B", "New ContentC11B");
        //Checkpoints
        Checkpoint expectedValue01 = checkpointService.modifyCheckpoint("pointIdA11", "New ContentA11");
        Checkpoint expectedValue02 = checkpointService.modifyCheckpoint("pointIdA12", "New ContentA12");
        Checkpoint expectedValue03 = checkpointService.modifyCheckpoint("pointIdA21", "New ContentA21");
        Checkpoint expectedValue04 = checkpointService.modifyCheckpoint("pointIdA22", "New ContentA22");
        Checkpoint expectedValue05 = checkpointService.modifyCheckpoint("pointIdB11", "New ContentB11");
        Checkpoint expectedValue06 = checkpointService.modifyCheckpoint("pointIdB12", "New ContentB12");
        Checkpoint expectedValue07 = checkpointService.modifyCheckpoint("pointIdB21", "New ContentB21");
        Checkpoint expectedValue08 = checkpointService.modifyCheckpoint("pointIdB22", "New ContentB22");
        Checkpoint expectedValue09 = checkpointService.modifyCheckpoint("pointIdC11", "New ContentC11");
        Checkpoint expectedValue10 = checkpointService.modifyCheckpoint("pointIdC12", "New ContentC12");
        Checkpoint expectedValue11 = checkpointService.modifyCheckpoint("pointIdD1", "New ContentD1");
        Checkpoint expectedValue12 = checkpointService.modifyCheckpoint("pointIdD2", "New ContentD2");

        //Then
        //Checkpoints
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdA11").get()).isEqualTo(expectedValue01);
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdA12").get()).isEqualTo(expectedValue02);
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdA21").get()).isEqualTo(expectedValue03);
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdA22").get()).isEqualTo(expectedValue04);
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdB11").get()).isEqualTo(expectedValue05);
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdB12").get()).isEqualTo(expectedValue06);
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdB21").get()).isEqualTo(expectedValue07);
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdB22").get()).isEqualTo(expectedValue08);
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdC11").get()).isEqualTo(expectedValue09);
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdC12").get()).isEqualTo(expectedValue10);
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdD1").get()).isEqualTo(expectedValue11);
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdD2").get()).isEqualTo(expectedValue12);
        //SubCheckpoints
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdA11A").get()).isEqualTo(expectedValue13);
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdA11B").get()).isEqualTo(expectedValue14);
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdB11A").get()).isEqualTo(expectedValue15);
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdB11B").get()).isEqualTo(expectedValue16);
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdC11A").get()).isEqualTo(expectedValue17);
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdC11B").get()).isEqualTo(expectedValue18);
    }

    @Test
    @DisplayName("Should Throw an ApiRequestException When Modifying a Non-Existent Checkpoint")
    public void throwExceptionModifyingNonExistentCheckpoint() {
        //When-Then
        Assertions.assertThatThrownBy(() -> {
            checkpointService.modifyCheckpoint("XXXXX", "New Content");
        }).isInstanceOf(ApiRequestException.class)
            .hasMessage("Checkpoint You Provided Doesn't Exist");
    }

    @Test
    @DisplayName("Should Delete Checkpoint and Subcheckpoints")
    public void shouldDeleteCheckpointAndSubcheckpoints() {
        //When 
        checkpointService.deleteCheckpoint("pointIdA11");
        checkpointService.deleteCheckpoint("pointIdA12");
        checkpointService.deleteCheckpoint("pointIdA21");
        checkpointService.deleteCheckpoint("pointIdA22");
        checkpointService.deleteCheckpoint("pointIdB11");
        checkpointService.deleteCheckpoint("pointIdB12");
        checkpointService.deleteCheckpoint("pointIdB21");
        checkpointService.deleteCheckpoint("pointIdB22");
        checkpointService.deleteCheckpoint("pointIdC11");
        checkpointService.deleteCheckpoint("pointIdC12");
        checkpointService.deleteCheckpoint("pointIdD1");
        checkpointService.deleteCheckpoint("pointIdD2");

        //Then
        Assertions.assertThat(checkpointRepository.findAll().isEmpty()).isTrue();
        Assertions.assertThat(checklistRepository.findChecklistByListId("listIdA1").get().getCheckpoints().isEmpty()).isTrue();
        Assertions.assertThat(checklistRepository.findChecklistByListId("listIdA2").get().getCheckpoints().isEmpty()).isTrue();
        Assertions.assertThat(checklistRepository.findChecklistByListId("listIdB1").get().getCheckpoints().isEmpty()).isTrue();
        Assertions.assertThat(checklistRepository.findChecklistByListId("listIdB2").get().getCheckpoints().isEmpty()).isTrue();
        Assertions.assertThat(checklistRepository.findChecklistByListId("listIdC1").get().getCheckpoints().isEmpty()).isTrue();
        Assertions.assertThat(checklistRepository.findChecklistByListId("listIdD").get().getCheckpoints().isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Should Only Delete Subcheckpoints")
    public void shouldDeleteSubcheckpointsOnly() {
        //When
        checkpointService.deleteCheckpoint("pointIdA11A");
        checkpointService.deleteCheckpoint("pointIdA11B");
        checkpointService.deleteCheckpoint("pointIdB11A");
        checkpointService.deleteCheckpoint("pointIdB11B");
        checkpointService.deleteCheckpoint("pointIdC11A");
        checkpointService.deleteCheckpoint("pointIdC11B");

        //Then
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdA11").get().getSubCheckpoints().isEmpty()).isTrue();
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdB11").get().getSubCheckpoints().isEmpty()).isTrue();
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdC11").get().getSubCheckpoints().isEmpty()).isTrue();
        Assertions.assertThat(checkpointRepository.findAll().size()).isEqualTo(12);
    }

    @Test
    @DisplayName("Should Throw a ApiRequestException When Deleteing Non-existent Checkpoint")
    public void throwExceptionNonexistentCheckpoint() {
        //When-Then
        Assertions.assertThatThrownBy(() -> {
            checkpointService.deleteCheckpoint("XXXXX");
        }).isInstanceOf(ApiRequestException.class)
            .hasMessage("Checkpoint You Wanted to Delete Doesn't Exist");
    }

    @Test
    @DisplayName("Should Turn an Existing Checkpoint into a SubCheckpoint Under Another Existing Checkpoint")
    public void shouldTurnExistingCheckpointToSubCheckpoint() {
        //When 
        checkpointService.turnIntoSubcheckpoint("listIdA2", "pointIdA21", "pointIdA22");

        //Then
        Assertions.assertThat(checkpointRepository
                .findCheckpointByPointId("pointIdA21")
                .get()
                .getSubCheckpoints()
                .contains(checkpointRepository
                    .findCheckpointByPointId("pointIdA22")
                    .get()))
            .isTrue();
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdA22").get().isSubpoint()).isTrue();
        Assertions.assertThat(checklistRepository.findChecklistByListId("listIdA2").get().getCheckpoints().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should Throw ApiRequestException When Turning an Existing Checkpoint into a SubCheckpoint Under a Non-Existent Checklist")
    public void throwExceptionTurningExistingCheckpointToSubCheckpointInNonExistentChecklist() {
        //When-Then
        Assertions.assertThatThrownBy(() -> {
            checkpointService.turnIntoSubcheckpoint("XXXXX", "pointIdA21", "pointIdA22");
        }).isInstanceOf(ApiRequestException.class)
            .hasMessage("Checklist You Provided Doesn't Exist");
    }

    @Test
    @DisplayName("Should Throw ApiRequestException When Turning a Non-Existent Checkpoint into a SubCheckpoint")
    public void throwExceptionTurningNonExistentCheckpointToSubCheckpoint() {
        //When-Then
        Assertions.assertThatThrownBy(() -> {
            checkpointService.turnIntoSubcheckpoint("listIdA2", "pointIdA21", "XXXXX");
        }).isInstanceOf(ApiRequestException.class)
            .hasMessage("SubCheckpoint You Provided Doesn't Exist");
    }

    @Test
    @DisplayName("Should Throw ApiRequestException When Turning an Existing Checkpoint into a SubCheckpoint Under Non-Existent Parent Checkpoint")
    public void throwExceptionTurningCheckpointToSubCheckpointUnderNonExistentParentCheckpoint() {
        //When-Then
        Assertions.assertThatThrownBy(() -> {
            checkpointService.turnIntoSubcheckpoint("listIdA2", "XXXXX", "pointIdA22");
        }).isInstanceOf(ApiRequestException.class)
            .hasMessage("Parent Checkpoint You Provided Doesn't Exist");
    }

    @Test
    @DisplayName("Should Add a New SubCheckpoint Under Another Existing Checkpoint")
    public void shouldAddNewSubCheckpointUnderExistingCheckpoint() {
        //When 
        checkpointService.newSubcheckpoint("pointIdA11", "New Subcheckpoint Under pointIdA11");

        //Then
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdA11").get().getSubCheckpoints().size()).isEqualTo(3);
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdA11").get().getSubCheckpoints().get(2).getContent()).isEqualTo("New Subcheckpoint Under pointIdA11");
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdA11").get().getSubCheckpoints().get(2).isSubpoint()).isTrue();
        Assertions.assertThat(checkpointRepository.findAll().contains(checkpointRepository.findCheckpointByPointId("pointIdA11").get().getSubCheckpoints().get(2))).isTrue();
    }

    @Test
    @DisplayName("Should Throw ApiRequestException Adding New SubCheckpoint Under Non-Existent Checkpoint")
    public void throwExceptionAddingNewSubCheckpointUnderNonExistentCheckpoint() {
        //When-Then
        Assertions.assertThatThrownBy(() -> {
            checkpointService.newSubcheckpoint("XXXXX", "New Subcheckpoint Under pointIdA11");
        }).isInstanceOf(ApiRequestException.class)
            .hasMessage("Parent Checkpoint You Provided Doesn't Exist");
    }

    @Test
    @DisplayName("Should Turn an Existing SubCheckpoint into a Checkpoint Under an Existing Checklist")
    public void shouldTurnExistingSubCheckpointToCheckpoint() {
        //When 
        checkpointService.reverseSubcheckpoint("listIdA1", "pointIdA11", "pointIdA11B");

        //Then
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdA11").get().getSubCheckpoints().size()).isEqualTo(1);
        Assertions.assertThat(checklistRepository.findChecklistByListId("listIdA1").get().getCheckpoints().size()).isEqualTo(3);
        Assertions.assertThat(checklistRepository.findChecklistByListId("listIdA1").get().getCheckpoints().get(2).isSubpoint()).isFalse();
        Assertions.assertThat(checkpointRepository.findAll().contains(checkpointRepository.findCheckpointByPointId("pointIdA11B").get())).isTrue();
    }

    @Test
    @DisplayName("Should Throw an ApiRequestException When Turning a Non-Existent SubCheckpoint into a Checkpoint Under an Existing Checklist")
    public void throwExceptionTurningNonExistentSubCheckpointToCheckpoint() {
        //When-Then
        Assertions.assertThatThrownBy(() -> {
            checkpointService.reverseSubcheckpoint("listIdA1", "pointIdA11", "XXXXX");
        }).isInstanceOf(ApiRequestException.class)
            .hasMessage("SubCheckpoint You Provided Doesn't Exist");
    }

    @Test
    @DisplayName("Should Throw an ApiRequestException When Turning an Existing SubCheckpoint into a Checkpoint Under an Non-Existent Checklist")
    public void throwExceptionTurningExistingSubCheckpointToCheckpointUnderNonExistentChecklist() {
        //When-Then
        Assertions.assertThatThrownBy(() -> {
            checkpointService.reverseSubcheckpoint("XXXXX", "pointIdA11", "pointIdA11B");
        }).isInstanceOf(ApiRequestException.class)
            .hasMessage("Checklist You Provided Doesn't Exist");
    }

    @Test
    @DisplayName("Should Throw an ApiRequestException When Turning an Existing SubCheckpoint into a Checkpoint Under a Non-Existent Checkpoint")
    public void throwExceptionTurningExistingSubCheckpointUnderNonExistentCheckpoint() {
        //When-Then
        Assertions.assertThatThrownBy(() -> {
            checkpointService.reverseSubcheckpoint("listIdA1", "XXXXX", "pointIdA11B");
        }).isInstanceOf(ApiRequestException.class)
            .hasMessage("Parent Checkpoint You Provided Doesn't Exist");
    }

    @Test
    @DisplayName("Should Convert a Checkpoint's isComplete to boolean true ")
    public void shouldConvertIsCompleteToTrue() {
        //When
        //Checkpoints
        checkpointService.completeCheckpoint("pointIdA11");
        checkpointService.completeCheckpoint("pointIdA12");
        checkpointService.completeCheckpoint("pointIdA21");
        checkpointService.completeCheckpoint("pointIdA22");
        checkpointService.completeCheckpoint("pointIdB11");
        checkpointService.completeCheckpoint("pointIdB12");
        checkpointService.completeCheckpoint("pointIdB21");
        checkpointService.completeCheckpoint("pointIdB22");
        checkpointService.completeCheckpoint("pointIdC11");
        checkpointService.completeCheckpoint("pointIdC12");
        checkpointService.completeCheckpoint("pointIdD1");
        checkpointService.completeCheckpoint("pointIdD2");
        //Subcheckpoints
        checkpointService.completeCheckpoint("pointIdA11A");
        checkpointService.completeCheckpoint("pointIdA11B");
        checkpointService.completeCheckpoint("pointIdB11A");
        checkpointService.completeCheckpoint("pointIdB11B");
        checkpointService.completeCheckpoint("pointIdC11A");
        checkpointService.completeCheckpoint("pointIdC11B");

        //Then
        //Checkpoints
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdA11").get().isComplete()).isTrue();
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdA12").get().isComplete()).isTrue();
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdA21").get().isComplete()).isTrue();
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdA22").get().isComplete()).isTrue();
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdB11").get().isComplete()).isTrue();
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdB12").get().isComplete()).isTrue();
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdB21").get().isComplete()).isTrue();
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdB22").get().isComplete()).isTrue();
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdC11").get().isComplete()).isTrue();
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdC12").get().isComplete()).isTrue();
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdD1").get().isComplete()).isTrue();
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdD2").get().isComplete()).isTrue();
        //Subcheckpoints
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdA11A").get().isComplete()).isTrue();
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdA11B").get().isComplete()).isTrue();
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdB11A").get().isComplete()).isTrue();
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdB11B").get().isComplete()).isTrue();
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdC11A").get().isComplete()).isTrue();
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdC11B").get().isComplete()).isTrue();

        //When
        //Checkpoints
        checkpointService.completeCheckpoint("pointIdA11");
        checkpointService.completeCheckpoint("pointIdA12");
        checkpointService.completeCheckpoint("pointIdA21");
        checkpointService.completeCheckpoint("pointIdA22");
        checkpointService.completeCheckpoint("pointIdB11");
        checkpointService.completeCheckpoint("pointIdB12");
        checkpointService.completeCheckpoint("pointIdB21");
        checkpointService.completeCheckpoint("pointIdB22");
        checkpointService.completeCheckpoint("pointIdC11");
        checkpointService.completeCheckpoint("pointIdC12");
        checkpointService.completeCheckpoint("pointIdD1");
        checkpointService.completeCheckpoint("pointIdD2");
        //Subcheckpoints
        checkpointService.completeCheckpoint("pointIdA11A");
        checkpointService.completeCheckpoint("pointIdA11B");
        checkpointService.completeCheckpoint("pointIdB11A");
        checkpointService.completeCheckpoint("pointIdB11B");
        checkpointService.completeCheckpoint("pointIdC11A");
        checkpointService.completeCheckpoint("pointIdC11B");

        //Then
        //Checkpoints
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdA11").get().isComplete()).isFalse();
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdA12").get().isComplete()).isFalse();
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdA21").get().isComplete()).isFalse();
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdA22").get().isComplete()).isFalse();
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdB11").get().isComplete()).isFalse();
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdB12").get().isComplete()).isFalse();
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdB21").get().isComplete()).isFalse();
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdB22").get().isComplete()).isFalse();
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdC11").get().isComplete()).isFalse();
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdC12").get().isComplete()).isFalse();
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdD1").get().isComplete()).isFalse();
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdD2").get().isComplete()).isFalse();
        //Subcheckpoints
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdA11A").get().isComplete()).isFalse();
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdA11B").get().isComplete()).isFalse();
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdB11A").get().isComplete()).isFalse();
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdB11B").get().isComplete()).isFalse();
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdC11A").get().isComplete()).isFalse();
        Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointIdC11B").get().isComplete()).isFalse();
    }

    @Test
    @DisplayName("Should Throw an ApiRequestException When Converting isComplete attribute in Non-Existent Checkpoint ")
    public void throwExceptionConvertingIsCompleteToTrueInNonExistentCheckpoint() {
        //When-Then
        Assertions.assertThatThrownBy(() -> {
            checkpointService.completeCheckpoint("XXXXX");
        }).isInstanceOf(ApiRequestException.class)
            .hasMessage("Checkpoint You Provided Doesn't Exist");
    }
}
