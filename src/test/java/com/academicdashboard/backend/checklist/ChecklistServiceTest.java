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
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class ChecklistServiceTest {

    @Mock
    private CheckpointService checkpointService;

    @Mock
    private ChecklistRepository listRepository;

    @Mock
    private MongoTemplate mongoTemplate;

    private ChecklistService serviceUnderTest;

    @BeforeEach
    public void setup() {
        serviceUnderTest = new ChecklistService(
                checkpointService, 
                listRepository, 
                mongoTemplate);
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
        
        Mockito.verify(mongoTemplate, Mockito.times(1)).findAndModify(
                ArgumentMatchers.any(Query.class), 
                ArgumentMatchers.any(Update.class), 
                ArgumentMatchers.any(FindAndModifyOptions.class), 
                ArgumentMatchers.any(Class.class));
    }

    @Test
    @DisplayName("Should Replace Checklist's Old Title with New Title")
    @SuppressWarnings("unchecked")
    public void modifyExistingChecklistTitle() {
        String listId = "testId";
        String title = "newTitle";
        Checklist expectedValue = new Checklist("testId", "newTitle");

        //Given 
        Mockito.when(mongoTemplate.findAndModify(
                    ArgumentMatchers.any(Query.class),
                    ArgumentMatchers.any(Update.class),
                    ArgumentMatchers.any(FindAndModifyOptions.class),
                    ArgumentMatchers.any(Class.class))).thenReturn(expectedValue);
        

        //When
        Checklist returnedValue = serviceUnderTest.modifyChecklist(listId, title);

        //Then
        assertThat(returnedValue.getTitle()).isEqualTo(expectedValue.getTitle());

    }

    // @Test
    // @DisplayName("Should Throw ApiRequestException When Modifying Non-existent Checklist")
    // public void throwExceptionNonexistentChecklist() {
    //
    // }

    // @Test
    // @DisplayName("Should Delete Existing Checklist")
    // public void deleteExistingChecklist() {
    //
    // }

    // @Test
    // @DisplayName("Should Throw ApiRequestException When Deleting Non-existent Checklist")
    // public void throwExceptionDeletingChecklist() {
    //
    // }

}
