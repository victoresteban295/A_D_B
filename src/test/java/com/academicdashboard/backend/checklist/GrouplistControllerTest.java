// package com.academicdashboard.backend.checklist;
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
// @WebMvcTest(controllers = GrouplistController.class)
// public class GrouplistControllerTest {
//
//     @MockBean
//     private GrouplistService grouplistService;
//
//     @Autowired
//     private MockMvc mockMvc;
//
//     @Test
//     @DisplayName("Should Return Newly Created Grouplist When Making a Post Request to endpoint - /api/grouplist/new/{userId}")
//     public void shouldCreateNewGrouplist() throws Exception {
//         Grouplist grouplist = new Grouplist("id001", "Grouplist Title");
//
//         Mockito.when(grouplistService.createGrouplist("ai49h5u84j98h3k2i5v2q", "Grouplist Title"))
//             .thenReturn(grouplist);
//
//         mockMvc.perform(MockMvcRequestBuilders.post("/api/grouplist/new/ai49h5u84j98h3k2i5v2q")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content("{\"title\": \"Grouplist Title\"}"))
//             .andExpect(MockMvcResultMatchers.status().is(201))
//             .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.groupId", Matchers.is(grouplist.getGroupId())))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is(grouplist.getTitle())))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.checklists", Matchers.is(grouplist.getChecklists())));
//     }
//
//     @Test
//     @DisplayName("Should Return Modified Grouplist When Making a Put Request to endpoint - /api/grouplist/modify/{groupId}")
//     public void shouldModifyGrouplist() throws Exception {
//         Grouplist grouplist = new Grouplist("id001", "New Grouplist Title");
//
//         Mockito.when(grouplistService.modifyGrouplist("id001", "New Grouplist Title"))
//             .thenReturn(grouplist);
//
//         mockMvc.perform(MockMvcRequestBuilders.put("/api/grouplist/modify/id001")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content("{\"title\": \"New Grouplist Title\"}"))
//             .andExpect(MockMvcResultMatchers.status().is(200))
//             .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.groupId", Matchers.is(grouplist.getGroupId())))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is(grouplist.getTitle())))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.checklists", Matchers.is(grouplist.getChecklists())));
//     }
//
//     @Test
//     @DisplayName("Should Return Grouplist With Added New Checklist When Making a Put Request to endpoint - /api/grouplist/addnew/{groupId}")
//     public void shouldAddNewChecklistToGrouplist() throws Exception {
//         Grouplist grouplist = new Grouplist("id001", "Grouplist Title");
//
//         Mockito.when(grouplistService.addNewToGrouplist("id001", "Checklist Title"))
//             .thenReturn(grouplist);
//
//         mockMvc.perform(MockMvcRequestBuilders.put("/api/grouplist/addnew/id001")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content("{\"title\": \"Checklist Title\"}"))
//             .andExpect(MockMvcResultMatchers.status().is(200))
//             .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.groupId", Matchers.is(grouplist.getGroupId())))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is(grouplist.getTitle())));
//     }
//
//     @Test
//     @DisplayName("Should Return Grouplist With Added Checklist When Making a Put Request to endpoint - /api/grouplist/addexist/{userId}")
//     public void shouldAddChecklistToGrouplist() throws Exception {
//         Grouplist grouplist = new Grouplist("id001", "Grouplist Title");
//
//         Mockito.when(grouplistService.addExistToGrouplist("ai49h5u84j98h3k2i5v2q", "id001", "listId01"))
//             .thenReturn(grouplist);
//
//         mockMvc.perform(MockMvcRequestBuilders.put("/api/grouplist/addexist/ai49h5u84j98h3k2i5v2q")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content("{\"groupId\": \"id001\", \"listId\": \"listId01\"}"))
//             .andExpect(MockMvcResultMatchers.status().is(200))
//             .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.groupId", Matchers.is(grouplist.getGroupId())))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is(grouplist.getTitle())))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.checklists", Matchers.is(grouplist.getChecklists())));
//     }
//
//     @Test
//     @DisplayName("Should Return Grouplist Whose Checklist Got Removed When Making a Put Request to endpoint - /api/grouplist/removefrom/{userId}")
//     public void shouldremoveChecklistFromGrouplist() throws Exception {
//         Grouplist grouplist = new Grouplist("id001", "Grouplist Title");
//
//         Mockito.when(grouplistService.removefromGrouplist("ai49h5u84j98h3k2i5v2q", "id001", "listId01"))
//             .thenReturn(grouplist);
//
//         mockMvc.perform(MockMvcRequestBuilders.put("/api/grouplist/removefrom/ai49h5u84j98h3k2i5v2q")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content("{\"groupId\": \"id001\", \"listId\": \"listId01\"}"))
//             .andExpect(MockMvcResultMatchers.status().is(200))
//             .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.groupId", Matchers.is(grouplist.getGroupId())))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is(grouplist.getTitle())))
//             .andExpect(MockMvcResultMatchers.jsonPath("$.checklists", Matchers.is(grouplist.getChecklists())));
//     }
//
//     @Test
//     @DisplayName("Should Return Only 204 Status Code When Making a Delete Request to endpoint - /api/grouplist/delete/{userId}")
//     public void shouldDeleteGrouplist() throws Exception {
//         mockMvc.perform(MockMvcRequestBuilders.delete("/api/grouplist/delete/12345")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content("{\"groupId\": \"id001\", \"deleteAll\": false}"))
//             .andExpect(MockMvcResultMatchers.status().is(204));
//     }
// }
