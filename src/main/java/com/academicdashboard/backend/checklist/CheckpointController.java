package com.academicdashboard.backend.checklist;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/checklist/checkpoint")
public class CheckpointController {

    @Autowired
    private CheckpointService checkpointService;

    @PostMapping("/{checklistTitle}")
    public ResponseEntity<Checkpoint> createCheckpoint(
            @RequestBody Map<String, String> payload,
            @PathVariable String checklistTitle) {
        
        return new ResponseEntity<Checkpoint>(
            
            checkpointService.createCheckpoint(
                    payload.get("content"),
                    checklistTitle
                ),
                HttpStatus.OK);   
    }
    
}
