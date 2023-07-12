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
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.academicdashboard.backend.exception.ApiRequestException;
import com.academicdashboard.backend.student.Student;
import com.academicdashboard.backend.user.User;
import com.academicdashboard.backend.user.UserRepository;

@Testcontainers
@DataMongoTest
@EnableMongoRepositories
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

    @Autowired
    private UserRepository userRepository;

    @Autowired //added
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
        mongoTemplate.dropCollection(Checklist.class);
        mongoTemplate.createCollection(Checklist.class);
        mongoTemplate.dropCollection(Student.class);
        mongoTemplate.createCollection(Student.class);
    }

    @Test
    @DisplayName("Should Create a New Grouplist Under Student")
    public void shouldCreateNewGrouplist() {

        //Create New User
        var user = User.builder()
            .userId("123973789abjdrfklwi75")
            .firstname("Victor")
            .lastname("Benitez")
            .checklists(new ArrayList<>())
            .grouplists(new ArrayList<>())
            .build();
        userRepository.save(user);
        
        //When
        grouplistService.createGrouplist("123973789abjdrfklwi75", "grouplistTitle");

        //Then
        Assertions.assertThat(grouplistRepository.findAll().get(0).getTitle())
            .isEqualTo("grouplistTitle");

        Assertions.assertThat(mongoTemplate.findAll(User.class).get(0).getGrouplists().get(0).getTitle())
            .isEqualTo("grouplistTitle");

        userRepository.delete(user);
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

    @Test
    @DisplayName("Should Create a New Checklist Under an Existing Grouplist")
    public void createNewChecklistUnderExistingGrouplist() {
        //Given
        this.grouplistRepository.insert(new Grouplist("id001", "Grouplist Title01"));
        this.grouplistRepository.insert(new Grouplist("id002", "Grouplist Title02"));
        this.grouplistRepository.insert(new Grouplist("id003", "Grouplist Title03"));

        //When 
        grouplistService.addNewToGrouplist("id001", "Checklist Title");

        //Then
        Grouplist returnedValue = this.grouplistRepository.findGrouplistByGroupId("id001").get();
        Assertions.assertThat(returnedValue.getChecklists().get(0).getTitle()).isEqualTo("Checklist Title");
        Assertions.assertThat(mongoTemplate.findAll(Checklist.class).size()).isEqualTo(1); //Assert No Duplication
    }

    @Test
    @DisplayName("Should Throw an ApiRequestException When Adding New Checklist to Non-existent Grouplist")
    public void throwExceptionAddingNewChecklistToNonExistingGrouplist() {
        //Given
        this.grouplistRepository.insert(new Grouplist("id001", "Grouplist Title01"));
        this.grouplistRepository.insert(new Grouplist("id002", "Grouplist Title02"));
        this.grouplistRepository.insert(new Grouplist("id003", "Grouplist Title03"));

        //Then
        Assertions.assertThatThrownBy(() -> {
            grouplistService.addNewToGrouplist("id004", "Checklist Title");
        }).isInstanceOf(ApiRequestException.class)
            .hasMessage("Grouplist You Wanted to Modify Doesn't Exist");
    }


    @Test
    @DisplayName("Should Add an Existing Checklist Under an Existing Grouplist")
    public void addExistingChecklistUnderExistingGrouplist() {
        //Given
        var user = User.builder()
            .userId("123973789abjdrfklwi75")
            .firstname("Victor")
            .lastname("Benitez")
            .checklists(new ArrayList<>())
            .grouplists(new ArrayList<>())
            .build();

        Checklist checklist01 = new Checklist("lId01", "Checklist Title 01");
        Checklist checklist02 = new Checklist("lId02", "Checklist Title 02");
        Checklist checklist03 = new Checklist("lId03", "Checklist Title 03");
        mongoTemplate.insert(checklist01);
        mongoTemplate.insert(checklist02);
        mongoTemplate.insert(checklist03);

        List<Checklist> checklists = new ArrayList<>();
        checklists.add(checklist01);
        checklists.add(checklist02);
        checklists.add(checklist03);

        user.setChecklists(checklists);
        userRepository.save(user);

        this.grouplistRepository.insert(new Grouplist("gId01", "Grouplist Title01"));
        this.grouplistRepository.insert(new Grouplist("gId02", "Grouplist Title02"));
        this.grouplistRepository.insert(new Grouplist("gId03", "Grouplist Title03"));

        //When 
        grouplistService.addExistToGrouplist("123973789abjdrfklwi75", "gId02", "lId03");

        //Then
        Grouplist returnedValue = this.grouplistRepository.findGrouplistByGroupId("gId02").get();
        Assertions.assertThat(returnedValue.getChecklists().get(0).getTitle()).isEqualTo("Checklist Title 03");
        Assertions.assertThat(mongoTemplate.findAll(User.class).get(0).getChecklists().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Should Throw Exception When Adding a Non-Existent Checklist Under an Existing Grouplist")
    public void shouldThrowExceptionAddingNonExistentChecklistUnderExistingGrouplist() {
        //Given
        var user = User.builder()
            .userId("123973789abjdrfklwi75")
            .firstname("Victor")
            .lastname("Benitez")
            .checklists(new ArrayList<>())
            .grouplists(new ArrayList<>())
            .build();

        Checklist checklist01 = new Checklist("lId01", "Checklist Title 01");
        Checklist checklist02 = new Checklist("lId02", "Checklist Title 02");
        Checklist checklist03 = new Checklist("lId03", "Checklist Title 03");
        mongoTemplate.insert(checklist01);
        mongoTemplate.insert(checklist02);
        mongoTemplate.insert(checklist03);

        List<Checklist> checklists = new ArrayList<>();
        checklists.add(checklist01);
        checklists.add(checklist02);
        checklists.add(checklist03);

        user.setChecklists(checklists);
        userRepository.save(user);

        this.grouplistRepository.insert(new Grouplist("gId01", "Grouplist Title01"));
        this.grouplistRepository.insert(new Grouplist("gId02", "Grouplist Title02"));
        this.grouplistRepository.insert(new Grouplist("gId03", "Grouplist Title03"));

        //Then
        Assertions.assertThatThrownBy(() -> {
            grouplistService.addExistToGrouplist("123973789abjdrfklwi75", "gId02", "lId04");
        }).isInstanceOf(ApiRequestException.class)
            .hasMessage("Checklist You Wanted to Modify Doesn't Exist");
    }

    @Test
    @DisplayName("Should Throw Exception When Adding an Existent Checklist Under a Non-Existent Grouplist")
    public void shouldThrowExceptionAddingExistingChecklistUnderNonExistentGrouplist() {
        //Given
        var user = User.builder()
            .userId("123973789abjdrfklwi75")
            .firstname("Victor")
            .lastname("Benitez")
            .checklists(new ArrayList<>())
            .grouplists(new ArrayList<>())
            .build();

        Checklist checklist01 = new Checklist("lId01", "Checklist Title 01");
        Checklist checklist02 = new Checklist("lId02", "Checklist Title 02");
        Checklist checklist03 = new Checklist("lId03", "Checklist Title 03");
        mongoTemplate.insert(checklist01);
        mongoTemplate.insert(checklist02);
        mongoTemplate.insert(checklist03);

        List<Checklist> checklists = new ArrayList<>();
        checklists.add(checklist01);
        checklists.add(checklist02);
        checklists.add(checklist03);

        user.setChecklists(checklists);
        userRepository.save(user);

        this.grouplistRepository.insert(new Grouplist("gId01", "Grouplist Title01"));
        this.grouplistRepository.insert(new Grouplist("gId02", "Grouplist Title02"));
        this.grouplistRepository.insert(new Grouplist("gId03", "Grouplist Title03"));

        //Then
        Assertions.assertThatThrownBy(() -> {
            grouplistService.addExistToGrouplist("123973789abjdrfklwi75", "gId04", "lId03");
        }).isInstanceOf(ApiRequestException.class)
            .hasMessage("Grouplist You Wanted to Modify Doesn't Exist");
    }

    @Test
    @DisplayName("Should Remove an Existing Checklist Under an Existing Grouplist")
    public void removeExistingChecklistUnderExistingGrouplist() {
        //Given
        var user = User.builder()
            .userId("123973789abjdrfklwi75")
            .firstname("Victor")
            .lastname("Benitez")
            .checklists(new ArrayList<>())
            .grouplists(new ArrayList<>())
            .build();

        //Grouplist with Checklists
        Grouplist grouplist01 = new Grouplist("gId01", "Grouplist Title01");

        //Create Different Checklists
        Checklist checklist01 = new Checklist("lId01", "Checklist Title 01");
        Checklist checklist02 = new Checklist("lId02", "Checklist Title 02");
        Checklist checklist03 = new Checklist("lId03", "Checklist Title 03");
        mongoTemplate.insert(checklist01);
        mongoTemplate.insert(checklist02);
        mongoTemplate.insert(checklist03);

        //Add Checklist to List and Add to Grouplist
        List<Checklist> checklists = new ArrayList<>();
        checklists.add(checklist01);
        checklists.add(checklist02);
        checklists.add(checklist03);
        grouplist01.setChecklists(checklists);

        //Create Different Grouplist & Add to Repo
        this.grouplistRepository.insert(grouplist01);
        Grouplist grouplist02 = this.grouplistRepository
            .insert(new Grouplist("gId02", "Grouplist Title02"));
        Grouplist grouplist03 = this.grouplistRepository
            .insert(new Grouplist("gId03", "Grouplist Title03"));

        //Add GroupList to List
        List<Grouplist> grouplists = new ArrayList<>();
        grouplists.add(grouplist01);
        grouplists.add(grouplist02);
        grouplists.add(grouplist03);

        user.setGrouplists(grouplists);
        userRepository.save(user);

        //When 
        grouplistService.removefromGrouplist("123973789abjdrfklwi75", "gId01", "lId03");

        //Then
        Assertions.assertThat(mongoTemplate.findAll(User.class)
                .get(0).getChecklists().get(0).getTitle())
            .isEqualTo("Checklist Title 03");
        Grouplist returnedValue = this.grouplistRepository.findGrouplistByGroupId("gId01").get();
        Assertions.assertThat(returnedValue.getChecklists().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Should Throw Exception When Removing a Non-Existent Checklist Under an Existing Grouplist")
    public void throwExceptionWhenRemovingNonExistentChecklistUnderExistingGrouplist() {
        //Given
        var user = User.builder()
            .userId("123973789abjdrfklwi75")
            .firstname("Victor")
            .lastname("Benitez")
            .checklists(new ArrayList<>())
            .grouplists(new ArrayList<>())
            .build();

        //Grouplist with Checklists
        Grouplist grouplist01 = new Grouplist("gId01", "Grouplist Title01");

        //Create Different Checklists
        Checklist checklist01 = new Checklist("lId01", "Checklist Title 01");
        Checklist checklist02 = new Checklist("lId02", "Checklist Title 02");
        Checklist checklist03 = new Checklist("lId03", "Checklist Title 03");
        mongoTemplate.insert(checklist01);
        mongoTemplate.insert(checklist02);
        mongoTemplate.insert(checklist03);

        //Add Checklist to List and Add to Grouplist
        List<Checklist> checklists = new ArrayList<>();
        checklists.add(checklist01);
        checklists.add(checklist02);
        checklists.add(checklist03);
        grouplist01.setChecklists(checklists);

        //Create Different Grouplist & Add to Repo
        this.grouplistRepository.insert(grouplist01);
        Grouplist grouplist02 = this.grouplistRepository
            .insert(new Grouplist("gId02", "Grouplist Title02"));
        Grouplist grouplist03 = this.grouplistRepository
            .insert(new Grouplist("gId03", "Grouplist Title03"));

        //Add GroupList to List
        List<Grouplist> grouplists = new ArrayList<>();
        grouplists.add(grouplist01);
        grouplists.add(grouplist02);
        grouplists.add(grouplist03);

        user.setGrouplists(grouplists); 
        userRepository.save(user); 

        //Then
        Assertions.assertThatThrownBy(() -> {
            grouplistService.removefromGrouplist("123973789abjdrfklwi75", "gId01", "lId04");
        }).isInstanceOf(ApiRequestException.class)
            .hasMessage("Checklist You Wanted to Modify Doesn't Exist");
    }

    @Test
    @DisplayName("Should Throw Exception When Removing an Existing Checklist Under a Non-Existent Grouplist")
    public void throwExceptionWhenRemovingExistingChecklistUnderNonExistentGrouplist() {
        //Given
        var user = User.builder()
            .userId("123973789abjdrfklwi75")
            .firstname("Victor")
            .lastname("Benitez")
            .checklists(new ArrayList<>())
            .grouplists(new ArrayList<>())
            .build();

        //Grouplist with Checklists
        Grouplist grouplist01 = new Grouplist("gId01", "Grouplist Title01");

        //Create Different Checklists
        Checklist checklist01 = new Checklist("lId01", "Checklist Title 01");
        Checklist checklist02 = new Checklist("lId02", "Checklist Title 02");
        Checklist checklist03 = new Checklist("lId03", "Checklist Title 03");
        mongoTemplate.insert(checklist01);
        mongoTemplate.insert(checklist02);
        mongoTemplate.insert(checklist03);

        //Add Checklist to List and Add to Grouplist
        List<Checklist> checklists = new ArrayList<>();
        checklists.add(checklist01);
        checklists.add(checklist02);
        checklists.add(checklist03);
        grouplist01.setChecklists(checklists);

        //Create Different Grouplist & Add to Repo
        this.grouplistRepository.insert(grouplist01);
        Grouplist grouplist02 = this.grouplistRepository
            .insert(new Grouplist("gId02", "Grouplist Title02"));
        Grouplist grouplist03 = this.grouplistRepository
            .insert(new Grouplist("gId03", "Grouplist Title03"));

        //Add GroupList to List
        List<Grouplist> grouplists = new ArrayList<>();
        grouplists.add(grouplist01);
        grouplists.add(grouplist02);
        grouplists.add(grouplist03);

        user.setGrouplists(grouplists);
        userRepository.save(user);

        //Then
        Assertions.assertThatThrownBy(() -> {
            grouplistService.removefromGrouplist("123973789abjdrfklwi75", "gId04", "lId03");
        }).isInstanceOf(ApiRequestException.class)
            .hasMessage("Grouplist You Wanted to Modify Doesn't Exist");
    }

    @Test
    @DisplayName("Should Delete Grouplist without Deleteing it's Checklists")
    public void deleteGrouplistWithoutDeletingChecklists() {
        //Given
        var user = User.builder()
            .userId("123973789abjdrfklwi75")
            .firstname("Victor")
            .lastname("Benitez")
            .checklists(new ArrayList<>())
            .grouplists(new ArrayList<>())
            .build();

        //Grouplist with Checklists
        Grouplist grouplist01 = new Grouplist("gId01", "Grouplist Title01");

        //Create Different Checklists
        Checklist checklist01 = new Checklist("lId01", "Checklist Title 01");
        Checklist checklist02 = new Checklist("lId02", "Checklist Title 02");
        Checklist checklist03 = new Checklist("lId03", "Checklist Title 03");
        mongoTemplate.insert(checklist01);
        mongoTemplate.insert(checklist02);
        mongoTemplate.insert(checklist03);

        //Add Checklist to List and Add to Grouplist
        List<Checklist> checklists = new ArrayList<>();
        checklists.add(checklist01);
        checklists.add(checklist02);
        checklists.add(checklist03);
        grouplist01.setChecklists(checklists);

        //Create Different Grouplist & Add to Repo
        this.grouplistRepository.insert(grouplist01);
        Grouplist grouplist02 = this.grouplistRepository
            .insert(new Grouplist("gId02", "Grouplist Title02"));
        Grouplist grouplist03 = this.grouplistRepository
            .insert(new Grouplist("gId03", "Grouplist Title03"));

        //Add GroupList to List
        List<Grouplist> grouplists = new ArrayList<>();
        grouplists.add(grouplist01);
        grouplists.add(grouplist02);
        grouplists.add(grouplist03);

        user.setGrouplists(grouplists);
        userRepository.save(user);

        //When 
        grouplistService.deleteGrouplist("123973789abjdrfklwi75", "gId01", false);

        //Then
        Assertions.assertThat(mongoTemplate.findAll(User.class).get(0).getChecklists().size()).isEqualTo(3);
        Assertions.assertThat(mongoTemplate.findAll(User.class).get(0).getGrouplists().size()).isEqualTo(2);
        Assertions.assertThat(mongoTemplate.findAll(Checklist.class).size()).isEqualTo(3);
    }


    @Test
    @DisplayName("Should Delete Grouplist And Delete it's Checklists")
    public void deleteGrouplistAndDeleteChecklists() {
        //Given
        var user = User.builder()
            .userId("123973789abjdrfklwi75")
            .firstname("Victor")
            .lastname("Benitez")
            .checklists(new ArrayList<>())
            .grouplists(new ArrayList<>())
            .build();

        //Grouplist with Checklists
        Grouplist grouplist01 = new Grouplist("gId01", "Grouplist Title01");

        //Create Different Checklists
        Checklist checklist01 = new Checklist("lId01", "Checklist Title 01");
        Checklist checklist02 = new Checklist("lId02", "Checklist Title 02");
        Checklist checklist03 = new Checklist("lId03", "Checklist Title 03");
        mongoTemplate.insert(checklist01);
        mongoTemplate.insert(checklist02);
        mongoTemplate.insert(checklist03);

        //Add Checklist to List and Add to Grouplist
        List<Checklist> checklists = new ArrayList<>();
        checklists.add(checklist01);
        checklists.add(checklist02);
        checklists.add(checklist03);
        grouplist01.setChecklists(checklists);

        //Create Different Grouplist & Add to Repo
        this.grouplistRepository.insert(grouplist01);
        Grouplist grouplist02 = this.grouplistRepository
            .insert(new Grouplist("gId02", "Grouplist Title02"));
        Grouplist grouplist03 = this.grouplistRepository
            .insert(new Grouplist("gId03", "Grouplist Title03"));

        //Add GroupList to List
        List<Grouplist> grouplists = new ArrayList<>();
        grouplists.add(grouplist01);
        grouplists.add(grouplist02);
        grouplists.add(grouplist03);

        user.setGrouplists(grouplists);
        userRepository.save(user);

        //When 
        grouplistService.deleteGrouplist("123973789abjdrfklwi75", "gId01", true);

        //Then
        Assertions.assertThat(mongoTemplate.findAll(User.class).get(0).getChecklists().size()).isEqualTo(0);
        Assertions.assertThat(mongoTemplate.findAll(User.class).get(0).getGrouplists().size()).isEqualTo(2);
        Assertions.assertThat(mongoTemplate.findAll(Checklist.class).size()).isEqualTo(0);
    }
    
    @Test
    @DisplayName("Should Throw Exception When Deleting Non-Existent Grouplist")
    public void throwExceptionDeletingNonExistentGrouplist() {
        //Given
        var user = User.builder()
            .userId("123973789abjdrfklwi75")
            .firstname("Victor")
            .lastname("Benitez")
            .checklists(new ArrayList<>())
            .grouplists(new ArrayList<>())
            .build();

        //Grouplist with Checklists
        Grouplist grouplist01 = new Grouplist("gId01", "Grouplist Title01");

        //Create Different Checklists
        Checklist checklist01 = new Checklist("lId01", "Checklist Title 01");
        Checklist checklist02 = new Checklist("lId02", "Checklist Title 02");
        Checklist checklist03 = new Checklist("lId03", "Checklist Title 03");
        mongoTemplate.insert(checklist01);
        mongoTemplate.insert(checklist02);
        mongoTemplate.insert(checklist03);

        //Add Checklist to List and Add to Grouplist
        List<Checklist> checklists = new ArrayList<>();
        checklists.add(checklist01);
        checklists.add(checklist02);
        checklists.add(checklist03);
        grouplist01.setChecklists(checklists);

        //Create Different Grouplist & Add to Repo
        this.grouplistRepository.insert(grouplist01);
        Grouplist grouplist02 = this.grouplistRepository
            .insert(new Grouplist("gId02", "Grouplist Title02"));
        Grouplist grouplist03 = this.grouplistRepository
            .insert(new Grouplist("gId03", "Grouplist Title03"));

        //Add GroupList to List
        List<Grouplist> grouplists = new ArrayList<>();
        grouplists.add(grouplist01);
        grouplists.add(grouplist02);
        grouplists.add(grouplist03);

        user.setGrouplists(grouplists);
        userRepository.save(user);

        //Then
        Assertions.assertThatThrownBy(() -> {
            grouplistService.deleteGrouplist("123973789abjdrfklwi75", "gId04", true);
        }).isInstanceOf(ApiRequestException.class)
            .hasMessage("Grouplist You Wanted to Delete Doesn't Exist");
    }
}
