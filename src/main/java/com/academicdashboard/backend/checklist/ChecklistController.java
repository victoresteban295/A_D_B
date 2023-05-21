package com.academicdashboard.backend.checklist;

import java.util.Map;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/checklist")
public class ChecklistController {

    @Autowired
    private ChecklistService service;

    //Create New Checklist | Returns Checklist Created
    @PutMapping("/new/{userId}")
    public ResponseEntity<Checklist> createChecklist(
            @RequestBody Map<String, String> payload, 
            @PathVariable String userId) {

        return new ResponseEntity<Checklist>(
            service.createChecklist(
                    userId,
                    payload.get("title")
                ), 
                HttpStatus.CREATED);
    }

    //Modify Existing Checklist/Group | Returns Modified Checklist
    @PutMapping("/modify/{listId}")
    public ResponseEntity<Checklist> modifyChecklist(
            @RequestBody Map<String, String> payload,
            @PathVariable String listId) {

        return new ResponseEntity<Checklist>(
            service.modifyChecklist(
                    listId,
                    payload.get("title")
                ),
                HttpStatus.CREATED);
    }

    //Delete Existing Checklist | Returns Status Code 204
    @DeleteMapping("/delete/{listId}")
    public ResponseEntity<Void> deleteChecklist(
            @PathVariable String listId) {

        service.deleteChecklist(listId);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    //Create New Checklist Group | Returns Checklist Group Created
    @PutMapping("/new/group/{userId}")
    public ResponseEntity<Checklist> createChecklistGroup(
            @RequestBody Map<String, String> payload,
            @PathVariable String userId) {

        return new ResponseEntity<Checklist>(
            service.createChecklistGroup(
                userId,
                payload.get("title")
            ),
            HttpStatus.CREATED);
    }

    //Add Checklist to Group | Returns Checklist Group
    @PutMapping("/addto/group/{groupId}")
    public ResponseEntity<Checklist> addToChecklistGroup(
            @RequestBody Map<String, String> payload,
            @PathVariable String groupId) {

        return new ResponseEntity<Checklist>(
            service.addToChecklistGroup(
                groupId,
                payload.get("listId")
            ),
            HttpStatus.CREATED);
    }
    
    @PutMapping("/removefrom/group/{groupId}")
    public ResponseEntity<Checklist> removeFromChecklistGroup(
            @RequestBody Map<String, String> payload,
            @PathVariable String groupId) {

        return new ResponseEntity<Checklist>(
            service.removeFromChecklistGroup(
                groupId,
                payload.get("listId")
            ),
            HttpStatus.CREATED);
    }

    //Add Checkpoint to existing Checklist (Incomplete)
    @PutMapping("checkpoint/{title}")
    public ResponseEntity<Checklist> addCheckpoint(
            @RequestBody Map<String, String> payload,
            @PathVariable String title) {
        
        return new ResponseEntity<Checklist>(
            service.addCheckpoint(title, payload.get("content")), 
            HttpStatus.CREATED);
    }

    //Mark Checkpoint as Completed
}
