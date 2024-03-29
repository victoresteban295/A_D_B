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
@RequestMapping("/api/stud/grouplist")
@RequiredArgsConstructor
public class GrouplistController {

    private final GrouplistService grouplistService;

    //Create New Grouplist | Returns Grouplist Created
    @PostMapping("/new/{userId}")
    public ResponseEntity<Grouplist> createGrouplist(
            @RequestBody Map<String, String> payload,
            @PathVariable String userId) {

        return new ResponseEntity<Grouplist>(
            grouplistService.createGrouplist(
                userId,
                payload.get("title")
            ),
            HttpStatus.CREATED);
    }

    //Modify Existing Grouplist | Returns Modified Grouplist
    @PutMapping("/modify/{groupId}")
    public ResponseEntity<Grouplist> modifyGrouplist(
            @RequestBody Map<String, String> payload,
            @PathVariable String groupId) {

        return new ResponseEntity<Grouplist>(
            grouplistService.modifyGrouplist(
                    groupId,
                    payload.get("title")
                ),
                HttpStatus.OK);
    }

    //Add New Checklist to Grouplist | Returns Grouplist
    @PutMapping("/addnew/{groupId}")
    public ResponseEntity<Grouplist> addNewToGrouplist(
            @RequestBody Map<String, String> payload,
            @PathVariable String groupId) {

        return new ResponseEntity<Grouplist>(
            grouplistService.addNewToGrouplist(
                groupId,
                payload.get("title")
                ),
            HttpStatus.OK);
    }

    //Add Existing Checklist to Grouplist | Returns Grouplist
    @PutMapping("/addexist/{userId}")
    public ResponseEntity<Grouplist> addExistToGrouplist(
            @RequestBody Map<String, String> payload,
            @PathVariable String userId) {

        return new ResponseEntity<Grouplist>(
            grouplistService.addExistToGrouplist(
                userId, 
                payload.get("groupId"), 
                payload.get("listId")
            ),
            HttpStatus.OK);
    }

    //Remove Existing Checklist From Grouplist | Returns Modified Grouplist
    @PutMapping("/removefrom/{userId}")
    public ResponseEntity<Grouplist> removefromGrouplist(
            @RequestBody Map<String, String> payload,
            @PathVariable String userId) {

        return new ResponseEntity<Grouplist>(
            grouplistService.removefromGrouplist(
                userId, 
                payload.get("groupId"), 
                payload.get("listId")
            ),
            HttpStatus.OK);
    }

    record Condition(String groupId, boolean deleteAll){}

    //Delete Grouplist | Void
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Void> deleteGrouplist(
            @RequestBody Condition condition, 
            @PathVariable String userId) {

        grouplistService.deleteGrouplist(userId, condition.groupId(), condition.deleteAll());
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

}
