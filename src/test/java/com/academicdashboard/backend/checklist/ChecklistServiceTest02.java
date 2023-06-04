package com.academicdashboard.backend.checklist;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.academicdashboard.backend.exception.ApiRequestException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;

@ExtendWith(MockitoExtension.class)
public class ChecklistServiceTest02 {

    @Mock
    private CheckpointService checkpointService;

    @Mock
    private ChecklistRepository listRepository;

    @Mock
    private MongoTemplate mockedMongoTemplate;

    private ChecklistService serviceUnderTest;

    @BeforeEach
    public void setup() {
        serviceUnderTest = new ChecklistService(
                checkpointService, 
                listRepository, 
                mockedMongoTemplate);
    }

    @Test
    @DisplayName("Should Create a Ungrouped Checklist")
    @SuppressWarnings("unchecked")
    public void canCreateNewChecklist() {
        Checklist expectedValue = new Checklist("testId", "Title");

        //Given 
        String userId = "userId";
        String title = "Title";

        //When
        serviceUnderTest.createChecklist(userId, title);

        //Then
        ArgumentCaptor<Checklist> checklistArgumentCaptor = ArgumentCaptor.forClass(Checklist.class);
        Mockito.verify(listRepository, Mockito.times(1)).insert(checklistArgumentCaptor.capture());
        Checklist capturedChecklist = checklistArgumentCaptor.getValue();
        assertThat(capturedChecklist.getTitle()).isEqualTo(expectedValue.getTitle()); 
        
        Mockito.verify(mockedMongoTemplate, Mockito.times(1)).findAndModify(
                ArgumentMatchers.any(Query.class), 
                ArgumentMatchers.any(Update.class), 
                ArgumentMatchers.any(FindAndModifyOptions.class), 
                ArgumentMatchers.any(Class.class));
    }

    @Test
    @DisplayName("Should Return Modified Checklist")
    @SuppressWarnings("unchecked")
    public void modifyExistingChecklistTitle() {
        Checklist expectedValue = new Checklist("testId", "newTitle");

        //Given 
        Mockito.when(mockedMongoTemplate.findAndModify(
                    ArgumentMatchers.any(Query.class),
                    ArgumentMatchers.any(Update.class),
                    ArgumentMatchers.any(FindAndModifyOptions.class),
                    ArgumentMatchers.any(Class.class))).thenReturn(new Checklist("testId", "newTitle"));
        
        //When
        Checklist returnedValue = serviceUnderTest.modifyChecklist("testId", "newTitle");

        //Then
        assertThat(returnedValue).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("Should Throw ApiRequestException When Modifying Non-existent Checklist")
    @SuppressWarnings("unchecked")
    public void throwExceptionNonexistentChecklist() {

        //Given 
        Mockito.when(mockedMongoTemplate.findAndModify(
                    ArgumentMatchers.any(Query.class),
                    ArgumentMatchers.any(Update.class),
                    ArgumentMatchers.any(FindAndModifyOptions.class),
                    ArgumentMatchers.any(Class.class))).thenReturn(null);

        //Then
        Assertions.assertThatThrownBy(() -> {
            serviceUnderTest.modifyChecklist("testId", "title");
        }).isInstanceOf(ApiRequestException.class)
            .hasMessage("Checklist You Wanted to Modify Doesn't Exist");
    }

    @Test
    @DisplayName("Should Delete Existing Checklist")
    @SuppressWarnings("unchecked")
    public void deleteExistingChecklist() {
        Checklist checklist = new Checklist("listId", "title");
        List<Checkpoint> checkpoints = new ArrayList<>();
        checkpoints.add(new Checkpoint("id1", "content01", false, false));
        checkpoints.add(new Checkpoint("id2", "content02", false, false));
        checklist.setCheckpoints(checkpoints);

        //Given
        Mockito.when(mockedMongoTemplate.findOne(
                    ArgumentMatchers.any(Query.class),
                    ArgumentMatchers.any(Class.class)))
            .thenReturn(checklist);

        //When 
        serviceUnderTest.deleteChecklist("listId");

        //Given
        //Verify that checkpoints are being passsed to deleteCheckpoint to get deleted
        Mockito.verify(checkpointService, Mockito.times(checkpoints.size()))
            .deleteCheckpoint(ArgumentMatchers.anyString());

        Mockito.verify(mockedMongoTemplate, Mockito.times(1))
            .remove(ArgumentMatchers.any(Query.class), ArgumentMatchers.any(Class.class));

    }

    @Test
    @DisplayName("Should Throw ApiRequestException When Deleting Non-existent Checklist")
    @SuppressWarnings("unchecked")
    public void throwExceptionDeletingChecklist() {
        
        //Given
        Mockito.when(mockedMongoTemplate.findOne(
                    ArgumentMatchers.any(Query.class),
                    ArgumentMatchers.any(Class.class)))
            .thenReturn(null);

        //When
        Assertions.assertThatThrownBy(() -> {
            serviceUnderTest.deleteChecklist("listId");
        }).isInstanceOf(ApiRequestException.class)
            .hasMessage("Checklist You Wanted to Delete Doesn't Exist");
    }  

}
