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
@RequestMapping("api/checklist")
public class ChecklistController {

    @Autowired
    private ChecklistService checklistservice;

    @PostMapping("/{firstName}")
    public ResponseEntity<Checklist> createChecklist(
            @RequestBody Map<String, String> payload, 
            @PathVariable String firstName) {

        return new ResponseEntity<Checklist>(
                
            checklistservice.createChecklist(
                    payload.get("title"), 
                    payload.get("description"),
                    firstName
                ), 
                HttpStatus.OK);
    }
}
