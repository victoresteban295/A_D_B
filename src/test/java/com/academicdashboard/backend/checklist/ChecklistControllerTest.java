package com.academicdashboard.backend.checklist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@WebMvcTest(controllers = Checklist.class)
public class ChecklistControllerTest {

    @MockBean
    private ChecklistService checklistService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Should Return Newly Created Checklist When Making GET request to endpoints - /api/checklist/new/{userId} ")
    public void shouldCreateNewChecklist() throws Exception {
        //Mocked Response
        Checklist checklist = new Checklist("listId", "title");
        Checkpoint point01 = new Checkpoint("pointId01", "content01", false, false);
        Checkpoint point02 = new Checkpoint("pointId02", "content02", false, false);
        List<Checkpoint> points = new ArrayList<>();
        points.add(point01);
        points.add(point02);
        checklist.setCheckpoints(points);
        checklist.setId(new ObjectId());

        Mockito.when(checklistService.createChecklist("userId", "title"))
            .thenReturn(checklist);

        mockMvc.perform(get("/api/checklist/new/userId"));
    }
}
