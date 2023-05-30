package com.academicdashboard.backend.checklist;

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
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;

@Service
public class CheckpointService {

    @Autowired
    private CheckpointRepository repository;

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

    private static Update pushUpdate(String field, Checkpoint checkpoint) {
        return new Update().push(field).value(checkpoint);
    }

    private static Update pullUpdate(String field, Checkpoint checkpoint) {
        return new Update().pull(field, checkpoint); 
    }

    /*********** OPTION DEFINITION METHOD ***********/
    private static FindAndModifyOptions options(boolean returnNew, boolean upsert) {
        return new FindAndModifyOptions().returnNew(returnNew).upsert(upsert);
    }

    /*********** CRUD METHODS ***********/

    //Create New Checkpoint Into Existing Checklist | Returns Checklist
    public Checklist addCheckpoint(String listId, String content) {
        String pointId = publicId(5);
        Checkpoint checkpoint = repository.insert(new Checkpoint(pointId, content, false, false));

        return mongoTemplate.findAndModify(
                query("listId", listId), 
                pushUpdate("checkpoints", checkpoint), 
                options(true, true), 
                Checklist.class);
    }

    //Modify Existing Checkpoint | Returns Modified Checkpoint
    public Checkpoint modifyCheckpoint(String pointId, String content) {
        return Optional.ofNullable(
                mongoTemplate.findAndModify(
                        query("pointId",pointId), 
                        setUpdate("content", content), 
                        options(true, true), 
                        Checkpoint.class))
            .orElseThrow(() -> new ApiRequestException("Checkpoint Doesn't Exist"));
    }

    //Delete Checkpoint | Void
    public void deleteCheckpoint(String pointId) {
        Query query = query("pointId", pointId);

        //Does Checkpoint Exist?
        Optional.ofNullable(
                mongoTemplate.findOne(
                    query, 
                    Checkpoint.class))
            .orElseThrow(() -> new ApiRequestException("Checkpoint Doesn't Exist"));

        //Delete Checkpoint
        mongoTemplate.remove(
                query, 
                Checkpoint.class);
    }

    //Existing Checkpoint to Subcheckpoint | Return Checkpoint w/ Subpoints
    public Checkpoint turnIntoSubcheckpoint(String listId, String pointId, String subpointId) {

        //Get Existing Checkpoint to make into Subcheckpoint
        Checkpoint subpoint = Optional.ofNullable(
                mongoTemplate.findOne(
                        query("pointId", subpointId), 
                        Checkpoint.class))
            .orElseThrow(() -> new ApiRequestException("Checkpoint Doesn't Exist"));

        //Remove from Checklist's checkpoints atrribute
        mongoTemplate.findAndModify(
                query("listId", listId), 
                pullUpdate("checkpoints", subpoint), 
                Checklist.class);

        //Add SubCheckpoint to Checkpoint
        return Optional.ofNullable(
                mongoTemplate.findAndModify(
                        query("pointId", pointId), 
                        pushUpdate("subCheckpoints", subpoint), 
                        options(true, true), 
                        Checkpoint.class))
            .orElseThrow(() -> new ApiRequestException("Checkpoint Doesn't Exist"));
    }
    
    //Create New SubCheckpoint under Checkpoint | Return Checkpoint
    public Checkpoint newSubcheckpoint(String pointId, String content) {
        //Create New Checkpoint Object as Subcheckpoint
        String subpointId = publicId(5);
        Checkpoint subcheckpoint = repository.insert(new Checkpoint(subpointId, content, false, false));

        //Add Subcheckpoint to Checkpoint
        return Optional.ofNullable(
                mongoTemplate.findAndModify(
                        query("pointId", pointId), 
                        pushUpdate("subCheckpoints", subcheckpoint), 
                        options(true, true), 
                        Checkpoint.class))
            .orElseThrow(() -> new ApiRequestException("Parent Checkpoint Doesn't Exist"));
    }

    //Subcheckpoint to Checkpoint | Return Checklist
    public Checklist reverseSubcheckpoint(String listId, String pointId, String subpointId) {
        //Get Existing SubCheckpoint
        Checkpoint subpoint = Optional.ofNullable(
            mongoTemplate.findOne(
                    query("pointId", subpointId), 
                    Checkpoint.class))
            .orElseThrow(() -> new ApiRequestException("Sub-Checkpoint Doesn't Exist"));

        //Remove from Checkpoint's checkpoints atrribute
        Optional.ofNullable(mongoTemplate.findAndModify(
                query("pointId", pointId), 
                pullUpdate("subCheckpoints", subpoint), 
                Checklist.class))
            .orElseThrow(() -> new ApiRequestException("Parent Checkpoint Doesn't Exist"));

        //Add SubCheckpoint back to Checklist
        return Optional.ofNullable(
                mongoTemplate.findAndModify(
                        query("listId", listId), 
                        pushUpdate("checkpoints", subpoint), 
                        options(true, true), 
                        Checklist.class))
            .orElseThrow(() -> new ApiRequestException("Checklist Doesn't Exist"));
    }

    //Check off Complete Property on Checkpoint | Return Checkpoint
    public Checkpoint completeCheckpoint(String pointId) {
        Query query = query("pointId", pointId);
        Checkpoint checkpoint = Optional.ofNullable(
                mongoTemplate.findOne(
                        query, 
                        Checkpoint.class))
            .orElseThrow(() -> new ApiRequestException("Checkpoint Doesn't Exist"));

        return mongoTemplate.findAndModify(
                    query, 
                    new Update().set("isComplete", !checkpoint.isComplete()), 
                    options(true, true), 
                    Checkpoint.class);
    }

}
