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
@RequestMapping("api/checkpoint")
public class CheckpointController {

    @Autowired
    private CheckpointService service;

    //Create New Checkpoint Into Existing Checklist | Returns Checklist
    @PutMapping("/new/{listId}")
    public ResponseEntity<Checklist> addCheckpoint(
            @RequestBody Map<String, String> payload,
            @PathVariable String listId) {
        
        return new ResponseEntity<Checklist>(
            service.addCheckpoint(listId, payload.get("content")), 
            HttpStatus.CREATED);
    }


    //Modify Existing Checkpoint | Returns Modified Checkpoint
    @PutMapping("/modify/{pointId}")
    public ResponseEntity<Checkpoint> modifyCheckpoint(
            @RequestBody Map<String, String> payload, 
            @PathVariable String pointId) {

        return new ResponseEntity<Checkpoint>(
                service.modifyCheckpoint(
                    pointId, 
                    payload.get("content")
                ),
                HttpStatus.CREATED);
    }

    //Delete Checkpoint | Void
    @DeleteMapping("/delete/{pointId}")
    public ResponseEntity<Void> deleteCheckpoint(
            @PathVariable String pointId) {

        service.deleteCheckpoint(pointId);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    //Existing Checkpoint to Subcheckpoint | Return Checkpoint w/ Subpoints
    @PutMapping("/make/subpoint/{listId}")
    public ResponseEntity<Checkpoint> makeSubcheckpoint(
            @RequestBody Map<String, String> payload, 
            @PathVariable String listId) {

       return new ResponseEntity<Checkpoint>(
                service.makeSubcheckpoint(
                   listId, 
                   payload.get("pointId"),
                   payload.get("subpointId")
                ), 
               HttpStatus.CREATED);
    }

    //Create New SubCheckpoint under Checkpoint | Return Checkpoint
    @PutMapping("/new/subpoint/{pointId}")
    public ResponseEntity<Checkpoint> newSubcheckpoint(
            @RequestBody Map<String, String> payload, 
            @PathVariable String pointId) {

        return new ResponseEntity<Checkpoint>(
                service.newSubcheckpoint(
                    pointId, 
                    payload.get("content")
                ), 
                HttpStatus.CREATED);
    }

    //Subcheckpoint to Checkpoint | Return Checklist
    @PutMapping("/reverse/subpoint/{listId}")
    public ResponseEntity<Checklist> reverseSubcheckpoint(
            @RequestBody Map<String, String> payload, 
            @PathVariable String listId) {

        return new ResponseEntity<Checklist>(
                service.reverseSubcheckpoint(
                    listId, 
                    payload.get("pointId"),
                    payload.get("subpointId")
                ), 
                HttpStatus.CREATED);
    }

    //Check off Complete Property on Checkpoint | Return Checkpoint
    @PutMapping("/complete/{pointId}")
    public ResponseEntity<Checkpoint> completeCheckpoint(
            @PathVariable String pointId) {

        return new ResponseEntity<Checkpoint>(
                service.completeCheckpoint(pointId), 
                HttpStatus.CREATED);
    }

}
