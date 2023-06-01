package com.academicdashboard.backend.checklist;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.academicdashboard.backend.exception.ApiRequestException;
import com.academicdashboard.backend.student.Student;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ChecklistService {

    @Autowired
    private CheckpointService pointService;

    @Autowired
    private ChecklistRepository repository;

    @Autowired
    private MongoTemplate mongoTemplate;

    //Create New Public Id (JNanoId)
    private static String publicId(int size) {
        Random random = new Random();
        char[] alphabet = {'a','b','c','d','e','1','2','3','5'};
        return NanoIdUtils.randomNanoId(random, alphabet, size); //Create New Public Id
    }

    /*********** QUERY DEFINITION METHOD ***********/
    private static Query query(String field, String equalsValue) {
        return new Query().addCriteria(Criteria.where(field).is(equalsValue));
    } 

    /*********** UPDATE DEFINITION METHODS ***********/
    private static Update setUpdate(String field, String value) {
        return new Update().set(field, value);
    }

    private static Update pushUpdate(String field, Checklist checklist) {
        return new Update().push(field).value(checklist);
    }

    /*********** OPTION DEFINITION METHOD ***********/
    private static FindAndModifyOptions options(boolean returnNew, boolean upsert) {
        return new FindAndModifyOptions().returnNew(returnNew).upsert(upsert);
    }

    /*********** CRUD METHODS ***********/

    //Create New Checklist | Returns Checklist Created
    public Checklist createChecklist(String userId, String title) {
        String listId = publicId(5);
        Checklist checklist = repository.insert(new Checklist(listId, title)); //New Checklist 

        mongoTemplate.findAndModify(
                query("userId", userId), 
                pushUpdate("checklists", checklist), 
                options(true, true), 
                Student.class);

        return checklist;
    }

    //Modify Existing Checklist | Returns Modified Checklist
    public Checklist modifyChecklist(String listId, String newTitle) {
        return Optional.ofNullable(
                mongoTemplate.findAndModify(
                        query("listId", listId), 
                        setUpdate("title", newTitle), 
                        options(true, true), 
                        Checklist.class))
            .orElseThrow(() -> new ApiRequestException("Checklist Doesn't Exist"));
    }

    //Delete Existing Checklist | Void 
    public void deleteChecklist(String listId) {

        //Delete Corresponding Checkpoints
        Checklist checklist = Optional.ofNullable(
                mongoTemplate.findOne(
                        query("listId", listId), 
                        Checklist.class))
            .orElseThrow(() -> new ApiRequestException("Checklist Doesn't Exist"));

        List<Checkpoint> checkpoints = checklist.getCheckpoints(); 
        for(Checkpoint point : checkpoints) {
           pointService.deleteCheckpoint(point.getPointId());
        }

        //Delete Checklist
        mongoTemplate.remove(
                query("listId", listId), 
                Checklist.class);
    }

}
