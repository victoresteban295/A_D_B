package com.academicdashboard.backend.checklist;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.academicdashboard.backend.student.Student;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;

@Service
public class ChecklistService {

    @Autowired
    private ChecklistRepository repository;

    @Autowired
    private MongoTemplate mongoTemplate;

    private Random random = new Random();
    private char[] alphabet = {'a','b','c','d','e','1','2','3','5'};

    //Create New Checklist | Returns Checklist Created
    public Checklist createChecklist(String userId, String title) {
        String listId = NanoIdUtils.randomNanoId(random, alphabet, 5); //Create New Public Id
        Checklist checklist = repository.insert(new Checklist(listId, title)); //New Checklist 

        mongoTemplate.update(Student.class)
            .matching(Criteria.where("userId").is(userId))
            .apply(new Update().push("checklists").value(checklist))
            .first();

        return checklist;
    }

    //Modify Existing Checklist | Returns Modified Checklist
    public Checklist modifyChecklist(String listId, String newTitle) {
        Query query = new Query().addCriteria(Criteria.where("listId").is(listId));
        Update updateDef = new Update().set("title", newTitle);
        FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true).upsert(true); //Returns Modified Value

        return mongoTemplate.findAndModify(query, updateDef, options, Checklist.class);
    }

    //Delete Existing Checklist | Void 
    public void deleteChecklist(String listId) {
        repository.deleteChecklistByListId(listId);
    }

    //Add Checkpoint to Existing Checklist
    public Checklist createCheckpoint(String listId, String content) {
        String pointId = NanoIdUtils.randomNanoId(random, alphabet, 5); //Create New Public Id
        Checkpoint checkpoint = new Checkpoint(pointId, content, false, false);

        Query query = new Query().addCriteria(Criteria.where("listId").is(listId));
        Update updateDef = new Update().push("checkpoints").value(checkpoint);
        FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true).upsert(true); //Returns Modified Value

        return mongoTemplate.findAndModify(query, updateDef, options, Checklist.class);
    }

    //DONT FORGET TO LIMIT CHECKPOINT TO 50 PER CHECKLIST
    //Add Checkpoint to an existing Checklist
    public Checklist addCheckpoint(String title, String content) {
        String pubId = NanoIdUtils.randomNanoId(random, alphabet, 5); //Create New Public Id
        
        //Create Checkpoint
        Checkpoint checkpoint = new Checkpoint(pubId, content, false, false);

        Query query = new Query().addCriteria(Criteria.where("title").is(title));
        Update updateDef = new Update().push("toComplete").value(checkpoint);
        FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true).upsert(true); //Returns Modified Value

        return mongoTemplate.findAndModify(query, updateDef, options, Checklist.class);
    }
}
