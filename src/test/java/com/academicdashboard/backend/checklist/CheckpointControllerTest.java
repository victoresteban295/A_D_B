// package com.academicdashboard.backend.checklist;
//
// import java.util.ArrayList;
// import java.util.List;
//
// import org.hamcrest.Matchers;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.mockito.Mockito;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
// import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//
// @WebMvcTest(controllers = CheckpointController.class)
// public class CheckpointControllerTest {
//
//     @MockBean
//     private CheckpointService checkpointService;
//
//     @Autowired
//     private MockMvc mockMvc;
//
//     @Test
//     @DisplayName("Should Return Checklist With Added Checkpoint When Making a Post Request to endpoint - /api/checkpoint/new/{listId}")
//     public void shouldAddNewCheckpointUnderChecklist() throws Exception {
//         Checklist checklist = new Checklist("listId01", "Checklist Title01");
//         Checkpoint checkpoint = new Checkpoint("pointId01", "Checkpoint Content", false, false);
//         List<Checkpoint> checkpoints = new ArrayList<>();
//         checkpoints.add(checkpoint);
//         checklist.setCheckpoints(checkpoints);
//
//         Mockito.when(checkpointService.addCheckpoint("listId01", "Checkpoint Content"))
//             .thenReturn(checklist);
//
//         mockMvc.perform(MockMvcRequestBuilders.post("/api/checkpoint/new/listId01")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content("{\"content\": \"Checkpoint Content\"}"))
//             .andExpect(MockMvcResultMatchers.status().is(201))
//             .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.listId", Matchers.is(checklist.getListId())))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is(checklist.getTitle())))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.checkpoints", Matchers.hasSize(1)))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.checkpoints[0].pointId", Matchers.is(checkpoint.getPointId())))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.checkpoints[0].content", Matchers.is(checkpoint.getContent())))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.checkpoints[0].isComplete", Matchers.is(checkpoint.isComplete())))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.checkpoints[0].isSubpoint", Matchers.is(checkpoint.isSubpoint())));
//     }
//
//     @Test
//     @DisplayName("Should Return the Modified Checkpoint When Making a Put Request to endpoint - /api/checkpoint/modify/{pointId}")
//     public void shouldModifyCheckpoint() throws Exception {
//         Checkpoint checkpoint = new Checkpoint("pointId01", "Modified Checkpoint Content", false, false); 
//
//         Mockito.when(checkpointService.modifyCheckpoint("pointId01", "Modified Checkpoint Content"))
//             .thenReturn(checkpoint);
//
//         mockMvc.perform(MockMvcRequestBuilders.put("/api/checkpoint/modify/pointId01")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content("{\"content\": \"Modified Checkpoint Content\"}"))
//             .andExpect(MockMvcResultMatchers.status().is(200))
//             .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.pointId", Matchers.is(checkpoint.getPointId())))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.is(checkpoint.getContent())))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.isComplete", Matchers.is(checkpoint.isComplete())))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.isSubpoint", Matchers.is(checkpoint.isSubpoint())));
//     }
//
//     @Test
//     @DisplayName("Should Return Only 204 Status Code When Making a Delete Request to endpoint - /api/checkpoint/delete/{pointId}")
//     public void shouldDeleteCheckpoint() throws Exception {
//         mockMvc.perform(MockMvcRequestBuilders.delete("/api/checkpoint/delete/pointId01"))
//             .andExpect(MockMvcResultMatchers.status().is(204));
//     }
//
//     @Test
//     @DisplayName("Should Return Checkpoint with Added Subcheckpoint When Making a Put Request to endpoint - /api/checkpoint/make/subpoint/{listId}")
//     public void turnCheckpointToSubcheckpoint() throws Exception {
//         Checkpoint checkpoint = new Checkpoint("pointId01", "Parent Content", false, false); 
//         Checkpoint subpoint = new Checkpoint("pointId02", "Subpoint Content", false, true); 
//         List<Checkpoint> subpoints = new ArrayList<>();
//         subpoints.add(subpoint);
//         checkpoint.setSubCheckpoints(subpoints);
//
//         Mockito.when(checkpointService.turnIntoSubcheckpoint("listId01", "pointId01", "pointId02"))
//            .thenReturn(checkpoint);
//
//         mockMvc.perform(MockMvcRequestBuilders.put("/api/checkpoint/make/subpoint/listId01")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content("{\"pointId\": \"pointId01\", \"subpointId\": \"pointId02\"}"))
//             .andExpect(MockMvcResultMatchers.status().is(200))
//             .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.pointId", Matchers.is(checkpoint.getPointId())))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.is(checkpoint.getContent())))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.isComplete", Matchers.is(checkpoint.isComplete())))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.isSubpoint", Matchers.is(checkpoint.isSubpoint())))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.subCheckpoints", Matchers.hasSize(1)))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.subCheckpoints[0].pointId", Matchers.is(subpoint.getPointId())))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.subCheckpoints[0].content", Matchers.is(subpoint.getContent())))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.subCheckpoints[0].isComplete", Matchers.is(subpoint.isComplete())))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.subCheckpoints[0].isSubpoint", Matchers.is(subpoint.isSubpoint())));
//     }
//
//     @Test
//     @DisplayName("Should Return Checkpoint with Newly Added Subcheckpoint When Making a Put Request to endpoint - /api/checkpoint/new/subpoint/{pointId}")
//     public void shouldCreateNewSubCheckpointUnderCheckpoint() throws Exception {
//         Checkpoint checkpoint = new Checkpoint("pointId01", "Parent Content", false, false); 
//         Checkpoint subpoint = new Checkpoint("pointId02", "New Subpoint Content", false, true); 
//         List<Checkpoint> subpoints = new ArrayList<>();
//         subpoints.add(subpoint);
//         checkpoint.setSubCheckpoints(subpoints);
//
//         Mockito.when(checkpointService.newSubcheckpoint("pointId01", "New Subpoint Content"))
//            .thenReturn(checkpoint);
//
//         mockMvc.perform(MockMvcRequestBuilders.put("/api/checkpoint/new/subpoint/pointId01")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content("{\"content\": \"New Subpoint Content\"}"))
//             .andExpect(MockMvcResultMatchers.status().is(200))
//             .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.pointId", Matchers.is(checkpoint.getPointId())))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.is(checkpoint.getContent())))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.isComplete", Matchers.is(checkpoint.isComplete())))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.isSubpoint", Matchers.is(checkpoint.isSubpoint())))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.subCheckpoints", Matchers.hasSize(1)))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.subCheckpoints[0].pointId", Matchers.is(subpoint.getPointId())))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.subCheckpoints[0].content", Matchers.is(subpoint.getContent())))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.subCheckpoints[0].isComplete", Matchers.is(subpoint.isComplete())))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.subCheckpoints[0].isSubpoint", Matchers.is(subpoint.isSubpoint())));
//     }
//
//     @Test
//     @DisplayName("Should Return Checklist with Checkpoints When Making a Put Request to endpoint - /api/checkpoint/reverse/subpoint/{listId}")
//     public void shouldReverseSubCheckpointToCheckpoint() throws Exception {
//         Checklist checklist = new Checklist("listId01", "Checklist Title");
//         Checkpoint checkpoint01 = new Checkpoint("pointId01", "Checkpoint Content", false, false); 
//         Checkpoint checkpoint02 = new Checkpoint("pointId02", "SubCheckpoint to Checkpoint", false, true); 
//         List<Checkpoint> checkpoints = new ArrayList<>();
//         checkpoints.add(checkpoint01);
//         checkpoints.add(checkpoint02);
//         checklist.setCheckpoints(checkpoints);
//
//         Mockito.when(checkpointService.reverseSubcheckpoint("listId01", "pointId01", "pointId02"))
//            .thenReturn(checklist);
//
//         mockMvc.perform(MockMvcRequestBuilders.put("/api/checkpoint/reverse/subpoint/listId01")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content("{\"pointId\": \"pointId01\", \"subpointId\": \"pointId02\"}"))
//             .andExpect(MockMvcResultMatchers.status().is(200))
//             .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.listId", Matchers.is(checklist.getListId())))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is(checklist.getTitle())))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.checkpoints", Matchers.hasSize(2)))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.checkpoints[0].pointId", Matchers.is(checkpoint01.getPointId())))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.checkpoints[0].content", Matchers.is(checkpoint01.getContent())))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.checkpoints[0].isComplete", Matchers.is(checkpoint01.isComplete())))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.checkpoints[0].isSubpoint", Matchers.is(checkpoint01.isSubpoint())))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.checkpoints[0].subCheckpoints", Matchers.hasSize(0)))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.checkpoints[1].pointId", Matchers.is(checkpoint02.getPointId())))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.checkpoints[1].content", Matchers.is(checkpoint02.getContent())))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.checkpoints[1].isComplete", Matchers.is(checkpoint02.isComplete())))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.checkpoints[1].isSubpoint", Matchers.is(checkpoint02.isSubpoint())))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.checkpoints[1].subCheckpoints", Matchers.hasSize(0)));
//     }
//
//     @Test
//     @DisplayName("Should Return Checkpoint with isComplete Equal To True When Making a Put Request to endpoint - /api/checkpoint/new/subpoint/{pointId}")
//     public void shouldCompleteCheckpoint() throws Exception {
//         Checkpoint checkpoint = new Checkpoint("pointId01", "Checkpoint Content", true, false); 
//
//         Mockito.when(checkpointService.completeCheckpoint("pointId01"))
//            .thenReturn(checkpoint);
//
//         mockMvc.perform(MockMvcRequestBuilders.put("/api/checkpoint/complete/pointId01"))
//             .andExpect(MockMvcResultMatchers.status().is(200))
//             .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.pointId", Matchers.is(checkpoint.getPointId())))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.is(checkpoint.getContent())))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.isComplete", Matchers.is(checkpoint.isComplete())))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.isSubpoint", Matchers.is(checkpoint.isSubpoint())))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.subCheckpoints", Matchers.hasSize(0)));
//     }
// }
