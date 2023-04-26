package com.academicdashboard.backend.checklist;

import java.util.Map;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/checklist")
public class ChecklistController {

    @Autowired
    private ChecklistService service;

    //Get All Checklist (To Be Completed)
    //Each Time method is call, check the 'completed' checkpoints
    // and remove from database if they are passed a 24hrs old 
    @GetMapping()
    public ResponseEntity<Checklist> getAllChecklist() {
        return null;
    }

    //Create New Checklist
    @PostMapping("/{firstName}")
    public ResponseEntity<Checklist> createChecklist(
            @RequestBody Map<String, String> payload, 
            @PathVariable String firstName) {

        return new ResponseEntity<Checklist>(
                
            service.createChecklist(
                    payload.get("title"), 
                    firstName
                ), 
                HttpStatus.OK);
    }

    //Add Checkpoint to existing Checklist
    @PostMapping("checkpoint/{listId}")
    public ResponseEntity<Optional<Checklist>> addCheckpoint(
            @RequestBody Map<String, String> payload,
            @PathVariable ObjectId listId) {
        
        return new ResponseEntity<Optional<Checklist>>(
            service.createCheckpoint(listId, payload.get("content")), 
            HttpStatus.OK);
    }

    //Mark Checkpoint as Completed
}
