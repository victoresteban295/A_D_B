package com.academicdashboard.backend.checklist;

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
public class GrouplistServiceTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0.6");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private GrouplistRepository grouplistRepository;

    private GrouplistService grouplistService;

    @BeforeEach
    public void setUp() {
        this.grouplistService = new GrouplistService(
                grouplistRepository,
                mongoTemplate);
    }

    @AfterEach
    public void cleanup() {
        this.grouplistRepository.deleteAll();
    }

    @Test
    @DisplayName("Should Create a New Grouplist Under Student")
    public void shouldCreateNewGrouplist() {
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
        grouplistService.createGrouplist("123973789abjdrfklwi75", "grouplistTitle");

        //Then
        Assertions.assertThat(grouplistRepository.findAll().get(0).getTitle())
            .isEqualTo("grouplistTitle");

        Assertions.assertThat(mongoTemplate.findAll(Student.class).get(0).getGrouplists().get(0).getTitle())
            .isEqualTo("grouplistTitle");

        mongoTemplate.remove(student);
    }

    @Test
    @DisplayName("Should Modify an Existing Grouplist")
    public void modifyExistingGrouplistTitle() {
        //Given
        this.grouplistRepository.insert(new Grouplist("12345", "Old Grouplist"));

        //When 
        grouplistService.modifyGrouplist("12345", "New Grouplist Title");

        //Then
        Grouplist returnedValue = this.grouplistRepository.findGrouplistByGroupId("12345").get(); 
        Assertions.assertThat(returnedValue.getTitle()).isEqualTo("New Grouplist Title");
    }

    @Test
    @DisplayName("Should Throw an ApiRequestException When Modifying Non-existent Grouplist")
    public void throwExceptionModifyingNonexistentGrouplist() {
        //Given
        this.grouplistRepository.insert(new Grouplist("id01", "title01"));
        this.grouplistRepository.insert(new Grouplist("id02", "title02"));
        this.grouplistRepository.insert(new Grouplist("id03", "title03"));

        //Then
        Assertions.assertThatThrownBy(() -> {
            grouplistService.modifyGrouplist("id04", "title04");
        }).isInstanceOf(ApiRequestException.class)
            .hasMessage("Grouplist You Wanted to Modify Doesn't Exist");
    }

}
