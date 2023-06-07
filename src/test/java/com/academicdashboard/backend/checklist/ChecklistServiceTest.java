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
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.academicdashboard.backend.exception.ApiRequestException;
import com.academicdashboard.backend.student.Student;

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

    @Autowired
    private ChecklistRepository checklistRepository;

    private ChecklistService checklistService;

    @BeforeEach
    public void setUp() {
        this.checklistService = new ChecklistService(
                checklistRepository, 
                mongoTemplate);
    }

    @AfterEach
    public void cleanup() {
        this.checklistRepository.deleteAll();
    }

    @Test
    @DisplayName("Should Create a New Checklist Under Student")
    public void shouldCreateNewChecklist() {
        Student student = new Student(
                "123973789abjdrfklwi75", 
                "Victor", 
                "Benitez", 
                "March", 19, 1998, 
                "Albion College", 
                "Senor", 
                "Mathematics", "", "", 
                "emails@email.com", 
                "psword", 
                "3233459856");
        mongoTemplate.insert(student);

        //When
        checklistService.createChecklist("123973789abjdrfklwi75", "listTitle");

        //Then
        Assertions.assertThat(checklistRepository.findAll().get(0).getTitle()).isEqualTo("listTitle");

        Assertions.assertThat(mongoTemplate.findAll(Student.class)
                .get(0).getChecklists().get(0).getTitle()).isEqualTo("listTitle");

        mongoTemplate.remove(student);
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
    public void throwExceptionModifyingNonexistentChecklist() {
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

    @Test
    @DisplayName("Should Delete Checklist with its Checkpoints")
    public void shouldDeleteChecklistWithCheckpoints() {
        Checklist checklist = new Checklist("id01", "title01");
        Checkpoint point01 = mongoTemplate.save(new Checkpoint("12345", "content01", false, false));
        Checkpoint point02 = mongoTemplate.save(new Checkpoint("09876", "content02", false, false));
        List<Checkpoint> points = new ArrayList<>();
        points.add(point01);
        points.add(point02);
        checklist.setCheckpoints(points);

        //Given
        this.checklistRepository.insert(checklist);

        //When
        checklistService.deleteChecklist("id01");

        //Then
        Assertions.assertThat(mongoTemplate.findAll(Checkpoint.class).isEmpty()).isTrue();
        Assertions.assertThat(mongoTemplate.findAll(Checklist.class).isEmpty()).isTrue();
    }

    //Test Deleting Checklist with no checkpoints
    @Test
    @DisplayName("Should Delete Checklist")
    public void shouldDeleteChecklist() {
        Checklist checklist = new Checklist("id01", "title01");

        //Given
        this.checklistRepository.insert(checklist);

        //When
        checklistService.deleteChecklist("id01");

        //Then
        Assertions.assertThat(mongoTemplate.findAll(Checklist.class).isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Should Throw an ApiRequestException When Modifying Non-existent Checklist")
    public void throwExceptionDeletingNonexistentChecklist() {
        //Given
        this.checklistRepository.insert(new Checklist("id01", "title01"));
        this.checklistRepository.insert(new Checklist("id02", "title02"));
         
        //Then
        Assertions.assertThatThrownBy(() -> {
            checklistService.deleteChecklist("id03");
        }).isInstanceOf(ApiRequestException.class)
            .hasMessage("Checklist You Wanted to Delete Doesn't Exist");

    }
}
