package com.academicdashboard.backend.checklist;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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

    //Modify Existing Checklist | Returns Modified Checklist
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

    //Add Checkpoint | Returns Checklist
    @PutMapping("/new/checkpoint/{listId}")
    public ResponseEntity<Checklist> createCheckpoint(
            @RequestBody Map<String, String> payload, 
            @PathVariable String listId) {

        return new ResponseEntity<Checklist>(
                service.createCheckpoint(
                    listId, 
                    payload.get("content")
                ), 
                HttpStatus.CREATED);
    }
}
