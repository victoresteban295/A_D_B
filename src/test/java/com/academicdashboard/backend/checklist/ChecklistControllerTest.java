package com.academicdashboard.backend.checklist;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ChecklistController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ChecklistControllerTest {

    @MockBean
    private ChecklistService checklistService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "Victor", roles = "STUDENT")
    @DisplayName("Should Return Newly Created Checklist When Making POST request to endpoints - /api/checklist/new/{userId}")
    public void shouldCreateNewChecklist() throws Exception {
        Checklist checklist = new Checklist("12345", "Checklist Title");

        Mockito.when(checklistService.createChecklist("ai49h5u84j98h3k2i5v2q", "Checklist Title"))
            .thenReturn(checklist);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/checklist/new/ai49h5u84j98h3k2i5v2q")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\": \"Checklist Title\"}"))
            .andExpect(MockMvcResultMatchers.status().is(201))
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(MockMvcResultMatchers.jsonPath("$.listId", Matchers.is(checklist.getListId())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is(checklist.getTitle())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.checkpoints", Matchers.is(checklist.getCheckpoints())));
    }

    @Test
    @WithMockUser(username = "Victor", roles = "STUDENT")
    @DisplayName("Should Return Modified Checklist When Making PUT request to endpoints - /api/checklist/modify/{listId}")
    public void shouldModifyExistingChecklist() throws Exception {
        Checklist response = new Checklist("12345", "New Checklist Title");

        Mockito.when(checklistService.modifyChecklist("12345", "New Checklist Title"))
            .thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/checklist/modify/12345")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\": \"New Checklist Title\"}"))
            .andExpect(MockMvcResultMatchers.status().is(200))
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(MockMvcResultMatchers.jsonPath("$.listId", Matchers.is(response.getListId())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is(response.getTitle())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.checkpoints", Matchers.is(response.getCheckpoints())));
    }

    @Test
    @WithMockUser(username = "Victor", roles = "STUDENT")
    @DisplayName("Should Only Return 204 Status Code When Making DELETE request to endpoints - /api/checklist/delete/{listId}")
    public void shouldDeleteExistingChecklist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/checklist/delete/12345"))
            .andExpect(MockMvcResultMatchers.status().is(204));
    }
}
