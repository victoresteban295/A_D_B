// package com.academicdashboard.backend.checklist;
//
// import java.util.ArrayList;
// import java.util.List;
//
// import org.assertj.core.api.Assertions;
// import org.junit.Before;
// import org.junit.jupiter.api.AfterEach;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
// import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
// import org.springframework.data.mongodb.core.MongoTemplate;
// import org.springframework.test.context.DynamicPropertyRegistry;
// import org.springframework.test.context.DynamicPropertySource;
// import org.testcontainers.containers.MongoDBContainer;
// import org.testcontainers.junit.jupiter.Container;
// import org.testcontainers.junit.jupiter.Testcontainers;
//
// import com.academicdashboard.backend.exception.ApiRequestException;
//
// @Testcontainers
// @DataMongoTest
// @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// public class CheckpointServiceTest {
//
//     @Container
//     static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0.6");
//
//     // @DynamicPropertySource
//     // static void setProperties(DynamicPropertyRegistry registry) {
//     //     registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
//     // }
//
//     @Before
//     public void startup() {
//         mongoDBContainer.start();
//     }
//     
//     @Autowired
//     private MongoTemplate mongoTemplate;
//
//     @Autowired
//     private CheckpointRepository checkpointRepository;
//
//     private CheckpointService checkpointService;
//
//     @BeforeEach
//     public void setUp() {
//         this.checkpointService = new CheckpointService(
//                 checkpointRepository, 
//                 mongoTemplate);
//     }
//
//     @AfterEach
//     public void cleanup() {
//         this.checkpointRepository.deleteAll();
//         mongoTemplate.dropCollection(Checklist.class);
//         mongoTemplate.createCollection(Checklist.class);
//     }
//
//     @Test
//     @DisplayName("Should Create a New Checkpoint Under an Existing Checklist")
//     public void shouldCreateNewCheckpointUnderExistingChecklist() {
//         //Given
//         mongoTemplate.insert(new Checklist("listId01", "Checklist Title"));
//
//         //When
//         checkpointService.addCheckpoint("listId01", "Checkpoint Content");
//
//         //Then
//         Assertions.assertThat(mongoTemplate.findAll(Checklist.class)
//                 .get(0).getCheckpoints().get(0).getContent())
//             .isEqualTo("Checkpoint Content");
//
//         Assertions.assertThat(mongoTemplate.findAll(Checkpoint.class).size())
//             .isEqualTo(1);
//     }
//
//     @Test
//     @DisplayName("Should Throw an ApiRequestException When Creating a New Checkpoint Under a Non-Existent Checklist")
//     public void throwExceptionCreatingNewCheckpointUnderNonExistentChecklist() {
//         //Given
//         mongoTemplate.insert(new Checklist("listId01", "Checklist Title"));
//
//         //Then
//         Assertions.assertThatThrownBy(() -> {
//             checkpointService.addCheckpoint("listId02", "Checkpoint Content");
//         }).isInstanceOf(ApiRequestException.class)
//             .hasMessage("Checklist You Provided Doesn't Exist");
//     }
//
//     @Test
//     @DisplayName("Should Modify an Existing Checkpoint")
//     public void shouldModifyExistingCheckpoint() {
//         //Given
//         Checklist checklist = new Checklist("listId01", "Checklist Title"); //Create Checklist 
//
//         //Create Checkpoint
//         Checkpoint checkpoint = mongoTemplate.insert(new Checkpoint("pointId01", "Old Content", false, false));
//         List<Checkpoint> checkpoints = new ArrayList<>(); //Create a List of Checkpoints
//         checkpoints.add(checkpoint); //Add Checkpoint to List
//         checklist.setCheckpoints(checkpoints); //Add List of Checkpoints to Checklist
//         mongoTemplate.insert(checklist); 
//
//         //When
//         checkpointService.modifyCheckpoint("pointId01", "New Content");
//
//         //Then
//         Assertions.assertThat(mongoTemplate.findAll(Checklist.class)
//                 .get(0).getCheckpoints().get(0).getContent())
//             .isEqualTo("New Content");
//
//         Assertions.assertThat(mongoTemplate.findAll(Checkpoint.class).size())
//             .isEqualTo(1);
//     }
//
//     @Test
//     @DisplayName("Should Throw an ApiRequestException When Modifying a Non-Existent Checkpoint")
//     public void throwExceptionModifyingNonExistentCheckpoint() {
//         //Given
//         Checklist checklist = new Checklist("listId01", "Checklist Title"); //Create Checklist 
//
//         //Create Checkpoint
//         Checkpoint checkpoint = mongoTemplate.insert(new Checkpoint("pointId01", "Old Content", false, false));
//         List<Checkpoint> checkpoints = new ArrayList<>(); //Create a List of Checkpoints
//         checkpoints.add(checkpoint); //Add Checkpoint to List
//         checklist.setCheckpoints(checkpoints); //Add List of Checkpoints to Checklist
//         mongoTemplate.insert(checklist); 
//
//         //Then
//         Assertions.assertThatThrownBy(() -> {
//             checkpointService.modifyCheckpoint("pointId02", "New Content");
//         }).isInstanceOf(ApiRequestException.class)
//             .hasMessage("Checkpoint You Provided Doesn't Exist");
//     }
//
//     @Test
//     @DisplayName("Should Delete Checkpoint")
//     public void shouldDeleteCheckpoint() {
//         //Given
//         Checklist checklist = new Checklist("listId01", "Checklist Title"); //Create Checklist 
//
//         //Create Checkpoint
//         Checkpoint checkpoint = mongoTemplate.insert(new Checkpoint("pointId01", "Checkpoint Content", false, false));
//         List<Checkpoint> checkpoints = new ArrayList<>(); //Create a List of Checkpoints
//         checkpoints.add(checkpoint); //Add Checkpoint to List
//         checklist.setCheckpoints(checkpoints); //Add List of Checkpoints to Checklist
//         mongoTemplate.insert(checklist); 
//
//         //When 
//         checkpointService.deleteCheckpoint("pointId01");
//
//         //Then
//         Assertions.assertThat(checkpointRepository.findAll().isEmpty()).isTrue();
//         Assertions.assertThat(mongoTemplate.findAll(Checklist.class).get(0).getCheckpoints().size()).isEqualTo(0);
//     }
//
//     @Test
//     @DisplayName("Should Throw a ApiRequestException When Deleteing Non-existent Checkpoint")
//     public void throwExceptionNonexistentCheckpoint() {
//         //Given
//         Checklist checklist = new Checklist("listId01", "Checklist Title"); //Create Checklist 
//
//         //Create Checkpoint
//         Checkpoint checkpoint = mongoTemplate.insert(new Checkpoint("pointId01", "Checkpoint Content", false, false));
//         List<Checkpoint> checkpoints = new ArrayList<>(); //Create a List of Checkpoints
//         checkpoints.add(checkpoint); //Add Checkpoint to List
//         checklist.setCheckpoints(checkpoints); //Add List of Checkpoints to Checklist
//         mongoTemplate.insert(checklist); 
//
//         //Then
//         Assertions.assertThatThrownBy(() -> {
//             checkpointService.deleteCheckpoint("pointId02");
//         }).isInstanceOf(ApiRequestException.class)
//             .hasMessage("Checkpoint You Wanted to Delete Doesn't Exist");
//     }
//
//     @Test
//     @DisplayName("Should Turn an Existing Checkpoint into a SubCheckpoint Under Another Existing Checkpoint")
//     public void shouldTurnExistingCheckpointToSubCheckpoint() {
//         //Given
//         Checklist checklist = new Checklist("listId01", "Checklist Title"); //Create Checklist 
//
//         //Create Checkpoint
//         Checkpoint checkpoint = mongoTemplate.insert(new Checkpoint("pointId01", "Checkpoint Content", false, false));
//         Checkpoint subCheckpoint = mongoTemplate.insert(new Checkpoint("pointId02", "Sub-Checkpoint Content", false, false));
//         List<Checkpoint> checkpoints = new ArrayList<>(); //Create a List of Checkpoints
//
//         //Add Checkpoints to List
//         checkpoints.add(checkpoint); 
//         checkpoints.add(subCheckpoint); 
//         checklist.setCheckpoints(checkpoints); //Add List of Checkpoints to Checklist
//         mongoTemplate.insert(checklist); 
//
//         //When 
//         checkpointService.turnIntoSubcheckpoint("listId01", "pointId01", "pointId02");
//
//         //Then
//         Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointId01")
//                 .get().getSubCheckpoints().get(0).getContent())
//             .isEqualTo("Sub-Checkpoint Content");
//
//         Assertions.assertThat(mongoTemplate.findAll(Checklist.class)
//                 .get(0).getCheckpoints().size())
//             .isEqualTo(1);
//
//         Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointId02")
//                 .get().isSubpoint())
//             .isEqualTo(true);
//     }
//
//     @Test
//     @DisplayName("Should Throw ApiRequestException When Turning an Existing Checkpoint into a SubCheckpoint Under a Non-Existent Checklist")
//     public void throwExceptionTurningExistingCheckpointToSubCheckpointInNonExistentChecklist() {
//         //Given
//         Checklist checklist = new Checklist("listId01", "Checklist Title"); //Create Checklist 
//
//         //Create Checkpoint
//         Checkpoint checkpoint = mongoTemplate.insert(new Checkpoint("pointId01", "Checkpoint Content", false, false));
//         Checkpoint subCheckpoint = mongoTemplate.insert(new Checkpoint("pointId02", "Sub-Checkpoint Content", false, false));
//         List<Checkpoint> checkpoints = new ArrayList<>(); //Create a List of Checkpoints
//
//         //Add Checkpoints to List
//         checkpoints.add(checkpoint); 
//         checkpoints.add(subCheckpoint); 
//         checklist.setCheckpoints(checkpoints); //Add List of Checkpoints to Checklist
//         mongoTemplate.insert(checklist); 
//
//         //Then
//         Assertions.assertThatThrownBy(() -> {
//             checkpointService.turnIntoSubcheckpoint("listId09", "pointId01", "pointId02");
//         }).isInstanceOf(ApiRequestException.class)
//             .hasMessage("Checklist You Provided Doesn't Exist");
//     }
//
//     @Test
//     @DisplayName("Should Throw ApiRequestException When Turning a Non-Existent Checkpoint into a SubCheckpoint")
//     public void throwExceptionTurningNonExistentCheckpointToSubCheckpoint() {
//         //Given
//         Checklist checklist = new Checklist("listId01", "Checklist Title"); //Create Checklist 
//
//         //Create Checkpoint
//         Checkpoint checkpoint = mongoTemplate.insert(new Checkpoint("pointId01", "Checkpoint Content", false, false));
//         Checkpoint subCheckpoint = mongoTemplate.insert(new Checkpoint("pointId02", "Sub-Checkpoint Content", false, false));
//         List<Checkpoint> checkpoints = new ArrayList<>(); //Create a List of Checkpoints
//
//         //Add Checkpoints to List
//         checkpoints.add(checkpoint); 
//         checkpoints.add(subCheckpoint); 
//         checklist.setCheckpoints(checkpoints); //Add List of Checkpoints to Checklist
//         mongoTemplate.insert(checklist); 
//
//         //Then
//         Assertions.assertThatThrownBy(() -> {
//             checkpointService.turnIntoSubcheckpoint("listId01", "pointId01", "pointId09");
//         }).isInstanceOf(ApiRequestException.class)
//             .hasMessage("SubCheckpoint You Provided Doesn't Exist");
//     }
//
//     @Test
//     @DisplayName("Should Throw ApiRequestException When Turning an Existing Checkpoint into a SubCheckpoint Under Non-Existent Parent Checkpoint")
//     public void throwExceptionTurningCheckpointToSubCheckpointUnderNonExistentParentCheckpoint() {
//         //Given
//         Checklist checklist = new Checklist("listId01", "Checklist Title"); //Create Checklist 
//
//         //Create Checkpoint
//         Checkpoint checkpoint = mongoTemplate.insert(new Checkpoint("pointId01", "Checkpoint Content", false, false));
//         Checkpoint subCheckpoint = mongoTemplate.insert(new Checkpoint("pointId02", "Sub-Checkpoint Content", false, false));
//         List<Checkpoint> checkpoints = new ArrayList<>(); //Create a List of Checkpoints
//
//         //Add Checkpoints to List
//         checkpoints.add(checkpoint); 
//         checkpoints.add(subCheckpoint); 
//         checklist.setCheckpoints(checkpoints); //Add List of Checkpoints to Checklist
//         mongoTemplate.insert(checklist); 
//
//         //Then
//         Assertions.assertThatThrownBy(() -> {
//             checkpointService.turnIntoSubcheckpoint("listId01", "pointId09", "pointId02");
//         }).isInstanceOf(ApiRequestException.class)
//             .hasMessage("Parent Checkpoint You Provided Doesn't Exist");
//     }
//
//     @Test
//     @DisplayName("Should Add a New SubCheckpoint Under Another Existing Checkpoint")
//     public void shouldAddNewSubCheckpointUnderExistingCheckpoint() {
//         //Given
//         mongoTemplate.insert(new Checkpoint("pointId01", "Checkpoint Content", false, false));
//
//         //When 
//         checkpointService.newSubcheckpoint("pointId01", "New Sub-Checkpoint Content");
//
//         //Then
//         Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointId01")
//                 .get().getSubCheckpoints().get(0).getContent())
//             .isEqualTo("New Sub-Checkpoint Content"); 
//
//         Assertions.assertThat(checkpointRepository.findAll().size())
//             .isEqualTo(2);
//     }
//
//     @Test
//     @DisplayName("Should Throw ApiRequestException Adding New SubCheckpoint Under Non-Existent Checkpoint")
//     public void throwExceptionAddingNewSubCheckpointUnderNonExistentCheckpoint() {
//         //Given
//         mongoTemplate.insert(new Checkpoint("pointId01", "Checkpoint Content", false, false));
//
//         //Then
//         Assertions.assertThatThrownBy(() -> {
//             checkpointService.newSubcheckpoint("pointId02", "New Sub-Checkpoint Content");
//         }).isInstanceOf(ApiRequestException.class)
//             .hasMessage("Parent Checkpoint You Provided Doesn't Exist");
//     }
//
//     @Test
//     @DisplayName("Should Turn an Existing SubCheckpoint into a Checkpoint Under an Existing Checklist")
//     public void shouldTurnExistingSubCheckpointToCheckpoint() {
//         //Given
//         Checklist checklist = new Checklist("listId01", "Checklist Title"); //Create Checklist 
//         Checkpoint checkpoint = new Checkpoint("pointId01", "Checkpoint Content", false, false);
//         Checkpoint subCheckpoint = mongoTemplate.insert(new Checkpoint("pointId02", "SubCheckpoint To Checkpoint", false, true));
//
//         List<Checkpoint> subcheckpoints = new ArrayList<>(); //Create a List of Subcheckpoints
//         subcheckpoints.add(subCheckpoint); //Add Subcheckpoint to List
//         checkpoint.setSubCheckpoints(subcheckpoints); //Add List of Subcheckpoints to Checkpoint
//         mongoTemplate.insert(checkpoint);
//
//         List<Checkpoint> checkpoints = new ArrayList<>(); //Create a List of Checkpoints
//         checkpoints.add(checkpoint); //Add Checkpoints to List 
//
//         checklist.setCheckpoints(checkpoints); //Add List of Checkpoints to Checklist
//         mongoTemplate.insert(checklist); 
//
//         //When 
//         checkpointService.reverseSubcheckpoint("listId01", "pointId01", "pointId02");
//
//         //Then
//         Assertions.assertThat(mongoTemplate.findAll(Checklist.class)
//                 .get(0).getCheckpoints().size())
//             .isEqualTo(2);
//
//         Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointId01")
//                 .get().getSubCheckpoints().size())
//             .isEqualTo(0);
//     }
//
//     @Test
//     @DisplayName("Should Throw an ApiRequestException When Turning a Non-Existent SubCheckpoint into a Checkpoint Under an Existing Checklist")
//     public void throwExceptionTurningNonExistentSubCheckpointToCheckpoint() {
//         //Given
//         Checklist checklist = new Checklist("listId01", "Checklist Title"); //Create Checklist 
//         Checkpoint checkpoint = new Checkpoint("pointId01", "Checkpoint Content", false, false);
//         Checkpoint subCheckpoint = mongoTemplate.insert(new Checkpoint("pointId02", "SubCheckpoint To Checkpoint", false, true));
//
//         List<Checkpoint> subcheckpoints = new ArrayList<>(); //Create a List of Subcheckpoints
//         subcheckpoints.add(subCheckpoint); //Add Subcheckpoint to List
//         checkpoint.setSubCheckpoints(subcheckpoints); //Add List of Subcheckpoints to Checkpoint
//         mongoTemplate.insert(checkpoint);
//
//         List<Checkpoint> checkpoints = new ArrayList<>(); //Create a List of Checkpoints
//         checkpoints.add(checkpoint); //Add Checkpoints to List 
//
//         checklist.setCheckpoints(checkpoints); //Add List of Checkpoints to Checklist
//         mongoTemplate.insert(checklist); 
//
//         //Then
//         Assertions.assertThatThrownBy(() -> {
//             checkpointService.reverseSubcheckpoint("listId01", "pointId01", "pointId09");
//         }).isInstanceOf(ApiRequestException.class)
//             .hasMessage("SubCheckpoint You Provided Doesn't Exist");
//     }
//
//     @Test
//     @DisplayName("Should Throw an ApiRequestException When Turning an Existing SubCheckpoint into a Checkpoint Under an Non-Existent Checklist")
//     public void throwExceptionTurningExistingSubCheckpointToCheckpointUnderNonExistentChecklist() {
//         //Given
//         Checklist checklist = new Checklist("listId01", "Checklist Title"); //Create Checklist 
//         Checkpoint checkpoint = new Checkpoint("pointId01", "Checkpoint Content", false, false);
//         Checkpoint subCheckpoint = mongoTemplate.insert(new Checkpoint("pointId02", "SubCheckpoint To Checkpoint", false, true));
//
//         List<Checkpoint> subcheckpoints = new ArrayList<>(); //Create a List of Subcheckpoints
//         subcheckpoints.add(subCheckpoint); //Add Subcheckpoint to List
//         checkpoint.setSubCheckpoints(subcheckpoints); //Add List of Subcheckpoints to Checkpoint
//         mongoTemplate.insert(checkpoint);
//
//         List<Checkpoint> checkpoints = new ArrayList<>(); //Create a List of Checkpoints
//         checkpoints.add(checkpoint); //Add Checkpoints to List 
//
//         checklist.setCheckpoints(checkpoints); //Add List of Checkpoints to Checklist
//         mongoTemplate.insert(checklist); 
//
//         //Then
//         Assertions.assertThatThrownBy(() -> {
//             checkpointService.reverseSubcheckpoint("listId09", "pointId01", "pointId02");
//         }).isInstanceOf(ApiRequestException.class)
//             .hasMessage("Checklist You Provided Doesn't Exist");
//     }
//
//     @Test
//     @DisplayName("Should Throw an ApiRequestException When Turning an Existing SubCheckpoint into a Checkpoint Under a Non-Existent Checkpoint")
//     public void throwExceptionTurningExistingSubCheckpointUnderNonExistentCheckpoint() {
//         //Given
//         Checklist checklist = new Checklist("listId01", "Checklist Title"); //Create Checklist 
//         Checkpoint checkpoint = new Checkpoint("pointId01", "Checkpoint Content", false, false);
//         Checkpoint subCheckpoint = mongoTemplate.insert(new Checkpoint("pointId02", "SubCheckpoint To Checkpoint", false, true));
//
//         List<Checkpoint> subcheckpoints = new ArrayList<>(); //Create a List of Subcheckpoints
//         subcheckpoints.add(subCheckpoint); //Add Subcheckpoint to List
//         checkpoint.setSubCheckpoints(subcheckpoints); //Add List of Subcheckpoints to Checkpoint
//         mongoTemplate.insert(checkpoint);
//
//         List<Checkpoint> checkpoints = new ArrayList<>(); //Create a List of Checkpoints
//         checkpoints.add(checkpoint); //Add Checkpoints to List 
//
//         checklist.setCheckpoints(checkpoints); //Add List of Checkpoints to Checklist
//         mongoTemplate.insert(checklist); 
//
//         //Then
//         Assertions.assertThatThrownBy(() -> {
//             checkpointService.reverseSubcheckpoint("listId01", "pointId09", "pointId02");
//         }).isInstanceOf(ApiRequestException.class)
//             .hasMessage("Parent Checkpoint You Provided Doesn't Exist");
//     }
//
//     @Test
//     @DisplayName("Should Convert a Checkpoint's isComplete to boolean true ")
//     public void shouldConvertIsCompleteToTrue() {
//         //Given
//         mongoTemplate.insert(new Checkpoint("pointId01", "Checkpoint Content", false, false)); 
//
//         //When
//         checkpointService.completeCheckpoint("pointId01");
//
//         //Then
//         Assertions.assertThat(checkpointRepository.findCheckpointByPointId("pointId01")
//                 .get().isComplete())
//             .isTrue();
//     }
//
//     @Test
//     @DisplayName("Should Throw an ApiRequestException When Converting isComplete attribute in Non-Existent Checkpoint ")
//     public void throwExceptionConvertingIsCompleteToTrueInNonExistentCheckpoint() {
//         //Given
//         mongoTemplate.insert(new Checkpoint("pointId01", "Checkpoint Content", false, false)); 
//
//         //Then
//         Assertions.assertThatThrownBy(() -> {
//             checkpointService.completeCheckpoint("pointId09");
//         }).isInstanceOf(ApiRequestException.class)
//             .hasMessage("Checkpoint You Provided Doesn't Exist");
//     }
// }
