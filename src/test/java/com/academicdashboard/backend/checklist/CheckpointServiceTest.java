package com.academicdashboard.backend.checklist;

import java.util.ArrayList;
import java.util.List;

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
    }

    // @Test
    // @DisplayName("Should Throw an ApiRequestException When Modifying a Non-Existent Checkpoint")
    // public void throwExceptionModifyingNonExistentCheckpoint() {
    //     //Given
    //     Checklist checklist = Checklist.builder()
    //         .listId("listId01")
    //         .title("Checklist Title")
    //         .checkpoints(new ArrayList<>())
    //         .build();
    //
    //     //Create Checkpoint
    //     Checkpoint checkpoint = mongoTemplate.insert(
    //             Checkpoint.builder()
    //             .pointId("pointId01")
    //             .content("Old Content")
    //             .isComplete(false)
    //             .isSubpoint(false)
    //             .subCheckpoints(new ArrayList<>())
    //             .build()
    //             );
    //     List<Checkpoint> checkpoints = new ArrayList<>(); //Create a List of Checkpoints
    //     checkpoints.add(checkpoint); //Add Checkpoint to List
    //     checklist.setCheckpoints(checkpoints); //Add List of Checkpoints to Checklist
    //     mongoTemplate.insert(checklist); 
    //
    //     //Then
    //     Assertions.assertThatThrownBy(() -> {
    //         checkpointService.modifyCheckpoint("pointId02", "New Content");
    //     }).isInstanceOf(ApiRequestException.class)
    //         .hasMessage("Checkpoint You Provided Doesn't Exist");
    // }
    //
    // @Test
    // @DisplayName("Should Delete Checkpoint")
    // public void shouldDeleteCheckpoint() {
    //     //Given
    //     Checklist checklist = Checklist.builder()
    //         .listId("listId01")
    //         .title("Checklist Title")
    //         .checkpoints(new ArrayList<>())
    //         .build();
    //
    //     //Create Checkpoint
    //     Checkpoint checkpoint = mongoTemplate.insert(
    //             Checkpoint.builder()
    //             .pointId("pointId01")
    //             .content("Checkpoint Content")
    //             .isComplete(false)
    //             .isSubpoint(false)
    //             .subCheckpoints(new ArrayList<>())
    //             .build()
    //             );
    //     List<Checkpoint> checkpoints = new ArrayList<>(); //Create a List of Checkpoints
    //     checkpoints.add(checkpoint); //Add Checkpoint to List
    //     checklist.setCheckpoints(checkpoints); //Add List of Checkpoints to Checklist
    //     mongoTemplate.insert(checklist); 
    //
    //     //When 
    //     checkpointService.deleteCheckpoint("pointId01");
    //
    //     //Then
    //     Assertions.assertThat(checkpointRepository.findAll().isEmpty()).isTrue();
    //     Assertions.assertThat(mongoTemplate.findAll(Checklist.class).get(0).getCheckpoints().size()).isEqualTo(0);
    // }
    //
    // @Test
    // @DisplayName("Should Throw a ApiRequestException When Deleteing Non-existent Checkpoint")
    // public void throwExceptionNonexistentCheckpoint() {
    //     //Given
    //     Checklist checklist = Checklist.builder()
    //         .listId("listId01")
    //         .title("Checklist Title")
    //         .checkpoints(new ArrayList<>())
    //         .build();
    //
    //     //Create Checkpoint
    //     Checkpoint checkpoint = mongoTemplate.insert(
    //             Checkpoint.builder()
    //             .pointId("pointId01")
    //             .content("Checkpoint Content")
    //             .isComplete(false)
    //             .isSubpoint(false)
    //             .subCheckpoints(new ArrayList<>())
    //             .build()
    //             );
    //     List<Checkpoint> checkpoints = new ArrayList<>(); //Create a List of Checkpoints
    //     checkpoints.add(checkpoint); //Add Checkpoint to List
    //     checklist.setCheckpoints(checkpoints); //Add List of Checkpoints to Checklist
    //     mongoTemplate.insert(checklist); 
    //
    //     //Then
    //     Assertions.assertThatThrownBy(() -> {
    //         checkpointService.deleteCheckpoint("pointId02");
    //     }).isInstanceOf(ApiRequestException.class)
    //         .hasMessage("Checkpoint You Wanted to Delete Doesn't Exist");
    // }
    //
    // @Test
    // @DisplayName("Should Turn an Existing Checkpoint into a SubCheckpoint Under Another Existing Checkpoint")
    // public void shouldTurnExistingCheckpointToSubCheckpoint() {
    //     //Given
    //     Checklist checklist = Checklist.builder()
    //         .listId("listId01")
    //         .title("Checklist Title")
    //         .checkpoints(new ArrayList<>())
    //         .build();
    //
    //     //Create Checkpoint
    //     Checkpoint checkpoint = mongoTemplate.insert(
    //             Checkpoint.builder()
    //             .pointId("pointId01")
    //             .content("Checkpoint Content")
    //             .isComplete(false)
    //             .isSubpoint(false)
    //             .subCheckpoints(new ArrayList<>())
    //             .build()
    //             );
    //     Checkpoint subCheckpoint = mongoTemplate.insert(
    //             Checkpoint.builder()
    //             .pointId("pointId02")
    //             .content("Sub-Checkpoint Content")
    //             .isComplete(false)
    //             .isSubpoint(false)
    //             .subCheckpoints(new ArrayList<>())
    //             .build()
    //             );
    //     List<Checkpoint> checkpoints = new ArrayList<>(); //Create a List of Checkpoints
    //
    //     //Add Checkpoints to List
    //     checkpoints.add(checkpoint); 
    //     checkpoints.add(subCheckpoint); 
    //     checklist.setCheckpoints(checkpoints); //Add List of Checkpoints to Checklist
    //     mongoTemplate.insert(checklist); 
    //
    //     //When 
    //     checkpointService.turnIntoSubcheckpoint("listId01", "pointId01", "pointId02");
    //
    //     //Then
    //     Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointId01")
    //             .get().getSubCheckpoints().get(0).getContent())
    //         .isEqualTo("Sub-Checkpoint Content");
    //
    //     Assertions.assertThat(mongoTemplate.findAll(Checklist.class)
    //             .get(0).getCheckpoints().size())
    //         .isEqualTo(1);
    //
    //     Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointId02")
    //             .get().isSubpoint())
    //         .isEqualTo(true);
    // }
    //
    // @Test
    // @DisplayName("Should Throw ApiRequestException When Turning an Existing Checkpoint into a SubCheckpoint Under a Non-Existent Checklist")
    // public void throwExceptionTurningExistingCheckpointToSubCheckpointInNonExistentChecklist() {
    //     //Given
    //     Checklist checklist = Checklist.builder()
    //         .listId("listId01")
    //         .title("Checklist Title")
    //         .checkpoints(new ArrayList<>())
    //         .build();
    //
    //     //Create Checkpoint
    //     Checkpoint checkpoint = mongoTemplate.insert(
    //             Checkpoint.builder()
    //             .pointId("pointId01")
    //             .content("Checkpoint Content")
    //             .isComplete(false)
    //             .isSubpoint(false)
    //             .subCheckpoints(new ArrayList<>())
    //             .build()
    //             );
    //     Checkpoint subCheckpoint = mongoTemplate.insert(
    //             Checkpoint.builder()
    //             .pointId("pointId02")
    //             .content("Sub-Checkpoint Content")
    //             .isComplete(false)
    //             .isSubpoint(false)
    //             .subCheckpoints(new ArrayList<>())
    //             .build()
    //             );
    //     List<Checkpoint> checkpoints = new ArrayList<>(); //Create a List of Checkpoints
    //
    //     //Add Checkpoints to List
    //     checkpoints.add(checkpoint); 
    //     checkpoints.add(subCheckpoint); 
    //     checklist.setCheckpoints(checkpoints); //Add List of Checkpoints to Checklist
    //     mongoTemplate.insert(checklist); 
    //
    //     //Then
    //     Assertions.assertThatThrownBy(() -> {
    //         checkpointService.turnIntoSubcheckpoint("listId09", "pointId01", "pointId02");
    //     }).isInstanceOf(ApiRequestException.class)
    //         .hasMessage("Checklist You Provided Doesn't Exist");
    // }
    //
    // @Test
    // @DisplayName("Should Throw ApiRequestException When Turning a Non-Existent Checkpoint into a SubCheckpoint")
    // public void throwExceptionTurningNonExistentCheckpointToSubCheckpoint() {
    //     //Given
    //     Checklist checklist = Checklist.builder()
    //         .listId("listId01")
    //         .title("Checklist Title")
    //         .checkpoints(new ArrayList<>())
    //         .build();
    //
    //     //Create Checkpoint
    //     Checkpoint checkpoint = mongoTemplate.insert(
    //             Checkpoint.builder()
    //             .pointId("pointId01")
    //             .content("Checkpoint Content")
    //             .isComplete(false)
    //             .isSubpoint(false)
    //             .subCheckpoints(new ArrayList<>())
    //             .build()
    //             );
    //     Checkpoint subCheckpoint = mongoTemplate.insert(
    //             Checkpoint.builder()
    //             .pointId("pointId02")
    //             .content("Sub-Checkpoint Content")
    //             .isComplete(false)
    //             .isSubpoint(false)
    //             .subCheckpoints(new ArrayList<>())
    //             .build()
    //             );
    //     List<Checkpoint> checkpoints = new ArrayList<>(); //Create a List of Checkpoints
    //
    //     //Add Checkpoints to List
    //     checkpoints.add(checkpoint); 
    //     checkpoints.add(subCheckpoint); 
    //     checklist.setCheckpoints(checkpoints); //Add List of Checkpoints to Checklist
    //     mongoTemplate.insert(checklist); 
    //
    //     //Then
    //     Assertions.assertThatThrownBy(() -> {
    //         checkpointService.turnIntoSubcheckpoint("listId01", "pointId01", "pointId09");
    //     }).isInstanceOf(ApiRequestException.class)
    //         .hasMessage("SubCheckpoint You Provided Doesn't Exist");
    // }
    //
    // @Test
    // @DisplayName("Should Throw ApiRequestException When Turning an Existing Checkpoint into a SubCheckpoint Under Non-Existent Parent Checkpoint")
    // public void throwExceptionTurningCheckpointToSubCheckpointUnderNonExistentParentCheckpoint() {
    //     //Given
    //     Checklist checklist = Checklist.builder()
    //         .listId("listId01")
    //         .title("Checklist Title")
    //         .checkpoints(new ArrayList<>())
    //         .build();
    //
    //     //Create Checkpoint
    //     Checkpoint checkpoint = mongoTemplate.insert(
    //             Checkpoint.builder()
    //             .pointId("pointId01")
    //             .content("Checkpoint Content")
    //             .isComplete(false)
    //             .isSubpoint(false)
    //             .subCheckpoints(new ArrayList<>())
    //             .build()
    //             );
    //     Checkpoint subCheckpoint = mongoTemplate.insert(
    //             Checkpoint.builder()
    //             .pointId("pointId02")
    //             .content("Sub-Checkpoint Content")
    //             .isComplete(false)
    //             .isSubpoint(false)
    //             .subCheckpoints(new ArrayList<>())
    //             .build()
    //             );
    //     List<Checkpoint> checkpoints = new ArrayList<>(); //Create a List of Checkpoints
    //
    //     //Add Checkpoints to List
    //     checkpoints.add(checkpoint); 
    //     checkpoints.add(subCheckpoint); 
    //     checklist.setCheckpoints(checkpoints); //Add List of Checkpoints to Checklist
    //     mongoTemplate.insert(checklist); 
    //
    //     //Then
    //     Assertions.assertThatThrownBy(() -> {
    //         checkpointService.turnIntoSubcheckpoint("listId01", "pointId09", "pointId02");
    //     }).isInstanceOf(ApiRequestException.class)
    //         .hasMessage("Parent Checkpoint You Provided Doesn't Exist");
    // }
    //
    // @Test
    // @DisplayName("Should Add a New SubCheckpoint Under Another Existing Checkpoint")
    // public void shouldAddNewSubCheckpointUnderExistingCheckpoint() {
    //     //Given
    //     mongoTemplate.insert(
    //             Checkpoint.builder()
    //             .pointId("pointId01")
    //             .content("Checkpoint Content")
    //             .isComplete(false)
    //             .isSubpoint(false)
    //             .subCheckpoints(new ArrayList<>())
    //             .build()
    //             );
    //
    //     //When 
    //     checkpointService.newSubcheckpoint("pointId01", "New Sub-Checkpoint Content");
    //
    //     //Then
    //     Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointId01")
    //             .get().getSubCheckpoints().get(0).getContent())
    //         .isEqualTo("New Sub-Checkpoint Content"); 
    //
    //     Assertions.assertThat(checkpointRepository.findAll().size())
    //         .isEqualTo(2);
    // }
    //
    // @Test
    // @DisplayName("Should Throw ApiRequestException Adding New SubCheckpoint Under Non-Existent Checkpoint")
    // public void throwExceptionAddingNewSubCheckpointUnderNonExistentCheckpoint() {
    //     //Given
    //     mongoTemplate.insert(
    //             Checkpoint.builder()
    //             .pointId("pointId01")
    //             .content("Checkpoint Content")
    //             .isComplete(false)
    //             .isSubpoint(false)
    //             .subCheckpoints(new ArrayList<>())
    //             .build()
    //             );
    //
    //     //Then
    //     Assertions.assertThatThrownBy(() -> {
    //         checkpointService.newSubcheckpoint("pointId02", "New Sub-Checkpoint Content");
    //     }).isInstanceOf(ApiRequestException.class)
    //         .hasMessage("Parent Checkpoint You Provided Doesn't Exist");
    // }
    //
    // @Test
    // @DisplayName("Should Turn an Existing SubCheckpoint into a Checkpoint Under an Existing Checklist")
    // public void shouldTurnExistingSubCheckpointToCheckpoint() {
    //     //Given
    //     Checklist checklist = Checklist.builder()
    //         .listId("listId01")
    //         .title("Checklist Title")
    //         .checkpoints(new ArrayList<>())
    //         .build();
    //     Checkpoint checkpoint = Checkpoint.builder()
    //         .pointId("pointId01")
    //         .content("Checkpoint Content")
    //         .isComplete(false)
    //         .isSubpoint(false)
    //         .subCheckpoints(new ArrayList<>())
    //         .build();
    //     Checkpoint subCheckpoint = mongoTemplate.insert(
    //             Checkpoint.builder()
    //             .pointId("pointId02")
    //             .content("SubCheckpoint To Checkpoint")
    //             .isComplete(false)
    //             .isSubpoint(true)
    //             .subCheckpoints(new ArrayList<>())
    //             .build()
    //             );
    //
    //     List<Checkpoint> subcheckpoints = new ArrayList<>(); //Create a List of Subcheckpoints
    //     subcheckpoints.add(subCheckpoint); //Add Subcheckpoint to List
    //     checkpoint.setSubCheckpoints(subcheckpoints); //Add List of Subcheckpoints to Checkpoint
    //     mongoTemplate.insert(checkpoint);
    //
    //     List<Checkpoint> checkpoints = new ArrayList<>(); //Create a List of Checkpoints
    //     checkpoints.add(checkpoint); //Add Checkpoints to List 
    //
    //     checklist.setCheckpoints(checkpoints); //Add List of Checkpoints to Checklist
    //     mongoTemplate.insert(checklist); 
    //
    //     //When 
    //     checkpointService.reverseSubcheckpoint("listId01", "pointId01", "pointId02");
    //
    //     //Then
    //     Assertions.assertThat(mongoTemplate.findAll(Checklist.class)
    //             .get(0).getCheckpoints().size())
    //         .isEqualTo(2);
    //
    //     Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointId01")
    //             .get().getSubCheckpoints().size())
    //         .isEqualTo(0);
    // }
    //
    // @Test
    // @DisplayName("Should Throw an ApiRequestException When Turning a Non-Existent SubCheckpoint into a Checkpoint Under an Existing Checklist")
    // public void throwExceptionTurningNonExistentSubCheckpointToCheckpoint() {
    //     //Given
    //     Checklist checklist = Checklist.builder()
    //         .listId("listId01")
    //         .title("Checklist Title")
    //         .checkpoints(new ArrayList<>())
    //         .build();
    //     Checkpoint checkpoint = Checkpoint.builder()
    //         .pointId("pointId01")
    //         .content("Checkpoint Content")
    //         .isComplete(false)
    //         .isSubpoint(false)
    //         .subCheckpoints(new ArrayList<>())
    //         .build();
    //     Checkpoint subCheckpoint = mongoTemplate.insert(
    //             Checkpoint.builder()
    //             .pointId("pointId02")
    //             .content("SubCheckpoint To Checkpoint")
    //             .isComplete(false)
    //             .isSubpoint(true)
    //             .subCheckpoints(new ArrayList<>())
    //             .build()
    //             );
    //
    //     List<Checkpoint> subcheckpoints = new ArrayList<>(); //Create a List of Subcheckpoints
    //     subcheckpoints.add(subCheckpoint); //Add Subcheckpoint to List
    //     checkpoint.setSubCheckpoints(subcheckpoints); //Add List of Subcheckpoints to Checkpoint
    //     mongoTemplate.insert(checkpoint);
    //
    //     List<Checkpoint> checkpoints = new ArrayList<>(); //Create a List of Checkpoints
    //     checkpoints.add(checkpoint); //Add Checkpoints to List 
    //
    //     checklist.setCheckpoints(checkpoints); //Add List of Checkpoints to Checklist
    //     mongoTemplate.insert(checklist); 
    //
    //     //Then
    //     Assertions.assertThatThrownBy(() -> {
    //         checkpointService.reverseSubcheckpoint("listId01", "pointId01", "pointId09");
    //     }).isInstanceOf(ApiRequestException.class)
    //         .hasMessage("SubCheckpoint You Provided Doesn't Exist");
    // }
    //
    // @Test
    // @DisplayName("Should Throw an ApiRequestException When Turning an Existing SubCheckpoint into a Checkpoint Under an Non-Existent Checklist")
    // public void throwExceptionTurningExistingSubCheckpointToCheckpointUnderNonExistentChecklist() {
    //     //Given
    //     Checklist checklist = Checklist.builder()
    //         .listId("listId01")
    //         .title("Checklist Title")
    //         .checkpoints(new ArrayList<>())
    //         .build();
    //     Checkpoint checkpoint = Checkpoint.builder()
    //         .pointId("pointId01")
    //         .content("Checkpoint Content")
    //         .isComplete(false)
    //         .isSubpoint(false)
    //         .subCheckpoints(new ArrayList<>())
    //         .build();
    //     Checkpoint subCheckpoint = mongoTemplate.insert(
    //             Checkpoint.builder()
    //             .pointId("pointId02")
    //             .content("SubCheckpoint To Checkpoint")
    //             .isComplete(false)
    //             .isSubpoint(true)
    //             .subCheckpoints(new ArrayList<>())
    //             .build()
    //             );
    //
    //     List<Checkpoint> subcheckpoints = new ArrayList<>(); //Create a List of Subcheckpoints
    //     subcheckpoints.add(subCheckpoint); //Add Subcheckpoint to List
    //     checkpoint.setSubCheckpoints(subcheckpoints); //Add List of Subcheckpoints to Checkpoint
    //     mongoTemplate.insert(checkpoint);
    //
    //     List<Checkpoint> checkpoints = new ArrayList<>(); //Create a List of Checkpoints
    //     checkpoints.add(checkpoint); //Add Checkpoints to List 
    //
    //     checklist.setCheckpoints(checkpoints); //Add List of Checkpoints to Checklist
    //     mongoTemplate.insert(checklist); 
    //
    //     //Then
    //     Assertions.assertThatThrownBy(() -> {
    //         checkpointService.reverseSubcheckpoint("listId09", "pointId01", "pointId02");
    //     }).isInstanceOf(ApiRequestException.class)
    //         .hasMessage("Checklist You Provided Doesn't Exist");
    // }
    //
    // @Test
    // @DisplayName("Should Throw an ApiRequestException When Turning an Existing SubCheckpoint into a Checkpoint Under a Non-Existent Checkpoint")
    // public void throwExceptionTurningExistingSubCheckpointUnderNonExistentCheckpoint() {
    //     //Given
    //     Checklist checklist = Checklist.builder()
    //         .listId("listId01")
    //         .title("Checklist Title")
    //         .checkpoints(new ArrayList<>())
    //         .build();
    //     Checkpoint checkpoint = Checkpoint.builder()
    //         .pointId("pointId01")
    //         .content("Checkpoint Content")
    //         .isComplete(false)
    //         .isSubpoint(false)
    //         .subCheckpoints(new ArrayList<>())
    //         .build();
    //     Checkpoint subCheckpoint = mongoTemplate.insert(
    //             Checkpoint.builder()
    //             .pointId("pointId02")
    //             .content("SubCheckpoint To Checkpoint")
    //             .isComplete(false)
    //             .isSubpoint(true)
    //             .subCheckpoints(new ArrayList<>())
    //             .build()
    //             );
    //
    //     List<Checkpoint> subcheckpoints = new ArrayList<>(); //Create a List of Subcheckpoints
    //     subcheckpoints.add(subCheckpoint); //Add Subcheckpoint to List
    //     checkpoint.setSubCheckpoints(subcheckpoints); //Add List of Subcheckpoints to Checkpoint
    //     mongoTemplate.insert(checkpoint);
    //
    //     List<Checkpoint> checkpoints = new ArrayList<>(); //Create a List of Checkpoints
    //     checkpoints.add(checkpoint); //Add Checkpoints to List 
    //
    //     checklist.setCheckpoints(checkpoints); //Add List of Checkpoints to Checklist
    //     mongoTemplate.insert(checklist); 
    //
    //     //Then
    //     Assertions.assertThatThrownBy(() -> {
    //         checkpointService.reverseSubcheckpoint("listId01", "pointId09", "pointId02");
    //     }).isInstanceOf(ApiRequestException.class)
    //         .hasMessage("Parent Checkpoint You Provided Doesn't Exist");
    // }
    //
    // @Test
    // @DisplayName("Should Convert a Checkpoint's isComplete to boolean true ")
    // public void shouldConvertIsCompleteToTrue() {
    //     //Given
    //     mongoTemplate.insert(
    //             Checkpoint.builder()
    //             .pointId("pointId01")
    //             .content("Checkpoint Content")
    //             .isComplete(false)
    //             .isSubpoint(false)
    //             .subCheckpoints(new ArrayList<>())
    //             .build()
    //             ); 
    //
    //     //When
    //     checkpointService.completeCheckpoint("pointId01");
    //
    //     //Then
    //     Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointId01")
    //             .get().isComplete())
    //         .isTrue();
    // }
    //
    // @Test
    // @DisplayName("Should Throw an ApiRequestException When Converting isComplete attribute in Non-Existent Checkpoint ")
    // public void throwExceptionConvertingIsCompleteToTrueInNonExistentCheckpoint() {
    //     //Given
    //     mongoTemplate.insert(
    //             Checkpoint.builder()
    //             .pointId("pointId01")
    //             .content("Checkpoint Content")
    //             .isComplete(false)
    //             .isSubpoint(false)
    //             .subCheckpoints(new ArrayList<>())
    //             .build()
    //             ); 
    //
    //     //Then
    //     Assertions.assertThatThrownBy(() -> {
    //         checkpointService.completeCheckpoint("pointId09");
    //     }).isInstanceOf(ApiRequestException.class)
    //         .hasMessage("Checkpoint You Provided Doesn't Exist");
    // }
}
