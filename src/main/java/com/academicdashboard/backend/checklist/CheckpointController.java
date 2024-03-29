package com.academicdashboard.backend.checklist;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/stud/checkpoint")
@RequiredArgsConstructor
public class CheckpointController {

    private final CheckpointService checkpointService;

    //Create New Checkpoint Into Existing Checklist | Returns Checklist
    @PostMapping("/new/{listId}")
    public ResponseEntity<Checklist> addCheckpoint(
            @RequestBody Map<String, String> payload,
            @PathVariable String listId) {
        
        return new ResponseEntity<Checklist>(
            checkpointService.addCheckpoint(listId, payload.get("content")), 
            HttpStatus.CREATED);
    }

    //Modify Existing Checkpoint | Returns Modified Checkpoint
    @PutMapping("/modify/{pointId}")
    public ResponseEntity<Checkpoint> modifyCheckpoint(
            @RequestBody Map<String, String> payload, 
            @PathVariable String pointId) {

        return new ResponseEntity<Checkpoint>(
                checkpointService.modifyCheckpoint(
                    pointId, 
                    payload.get("content")
                ),
                HttpStatus.OK);
    }

    //Delete Checkpoint | Void
    @DeleteMapping("/delete/{pointId}")
    public ResponseEntity<Void> deleteCheckpoint(
            @PathVariable String pointId) {

        checkpointService.deleteCheckpoint(pointId);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    //Existing Checkpoint to Subcheckpoint | Return Checkpoint w/ Subpoints
    @PutMapping("/make/subpoint/{listId}")
    public ResponseEntity<Checkpoint> turnIntoSubcheckpoint(
            @RequestBody Map<String, String> payload, 
            @PathVariable String listId) {

       return new ResponseEntity<Checkpoint>(
                checkpointService.turnIntoSubcheckpoint(
                   listId, 
                   payload.get("pointId"),
                   payload.get("subpointId")
                ), 
               HttpStatus.OK);
    }

    //Create New SubCheckpoint under Checkpoint | Return Checkpoint
    @PutMapping("/new/subpoint/{pointId}")
    public ResponseEntity<Checkpoint> newSubcheckpoint(
            @RequestBody Map<String, String> payload, 
            @PathVariable String pointId) {

        return new ResponseEntity<Checkpoint>(
                checkpointService.newSubcheckpoint(
                    pointId, 
                    payload.get("content")
                ), 
                HttpStatus.OK);
    }

    //Subcheckpoint to Checkpoint | Return Checklist
    @PutMapping("/reverse/subpoint/{listId}")
    public ResponseEntity<Checklist> reverseSubcheckpoint(
            @RequestBody Map<String, String> payload, 
            @PathVariable String listId) {

        return new ResponseEntity<Checklist>(
                checkpointService.reverseSubcheckpoint(
                    listId, 
                    payload.get("pointId"),
                    payload.get("subpointId")
                ), 
                HttpStatus.OK);
    }

    //Check off Complete Property on Checkpoint | Return Checkpoint
    @PutMapping("/complete/{pointId}")
    public ResponseEntity<Checkpoint> completeCheckpoint(
            @PathVariable String pointId) {

        return new ResponseEntity<Checkpoint>(
                checkpointService.completeCheckpoint(pointId), 
                HttpStatus.OK);
    }

}
