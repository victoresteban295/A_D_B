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
import com.academicdashboard.backend.user.Student;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GrouplistService {

    @Autowired
    private GrouplistRepository repository;

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

    private static Update pullUpdate(String field, Checklist checklist) {
        return new Update().pull(field, checklist); 
    }

    /*********** OPTION DEFINITION METHOD ***********/
    private static FindAndModifyOptions options(boolean returnNew, boolean upsert) {
        return new FindAndModifyOptions().returnNew(returnNew).upsert(upsert);
    }

    /*********** CRUD METHODS ***********/

    //Create New Grouplist | Returns Grouplist Created
    public Grouplist createGrouplist(String userId, String title) {
        String groupId = publicId(5);
        Grouplist grouplist = repository.insert(new Grouplist(groupId, title));

        mongoTemplate.update(Student.class)
            .matching(Criteria.where("userId").is(userId))
            .apply(new Update().push("grouplists").value(grouplist))
            .first();

        return grouplist;
    }

    //Modify Existing Grouplist | Returns Modified Grouplist
    public Grouplist modifyGrouplist(String groupId, String newTitle) {
        if(mongoTemplate.exists(query("groupId", groupId), Grouplist.class)) {
            return mongoTemplate.findAndModify(
                query("groupId", groupId), 
                setUpdate("title", newTitle), 
                options(true, true), 
                Grouplist.class);
        } else {
            throw new ApiRequestException("Grouplist You Wanted to Modify Doesn't Exist");
        }
    }

    //Add New Checklist to Grouplist | Returns Grouplist
    public Grouplist addNewToGrouplist(String groupId, String listTitle) {
        String listId = publicId(5);
        Checklist checklist = mongoTemplate.insert(new Checklist(listId, listTitle));

        if(mongoTemplate.exists(query("groupId", groupId), Grouplist.class)) {
            return mongoTemplate.findAndModify(
                query("groupId", groupId), 
                pushUpdate("checklists", checklist), 
                options(true, true), 
                Grouplist.class);
        } else {
            throw new ApiRequestException("Grouplist You Wanted to Modify Doesn't Exist");
        }
    }

    //Add Existing Checklist to Grouplist | Returns Grouplist
    public Grouplist addExistToGrouplist(String userId, String groupId, String listId) {
        //Find Existing Checklist
        Checklist checklist = Optional.ofNullable(
                mongoTemplate.findOne(
                    query("listId", listId), 
                    Checklist.class))
            .orElseThrow(() -> new ApiRequestException("Checklist You Wanted to Modify Doesn't Exist"));

        //Remove Checklist Obj Reference from the User's checklists attribute
        mongoTemplate.findAndModify(
                query("userId", userId), 
                pullUpdate("checklists", checklist), 
                Student.class);

        if(mongoTemplate.exists(query("groupId", groupId), Grouplist.class)) {
            return mongoTemplate.findAndModify(
                query("groupId", groupId), 
                pushUpdate("checklists", checklist), 
                options(true, true), 
                Grouplist.class);
        } else {
            throw new ApiRequestException("Grouplist You Wanted to Modify Doesn't Exist");
        }
    }

    //Remove Existing Checklist From Grouplist | Returns Modified Grouplist
    public Grouplist removefromGrouplist(String userId, String groupId, String listId) {
        //Find Existing Checklist
        Checklist checklist = Optional.ofNullable(
                mongoTemplate.findOne(
                    query("listId", listId), 
                    Checklist.class))
            .orElseThrow(() -> new ApiRequestException("Checklist You Wanted to Modify Doesn't Exist"));

        if(mongoTemplate.exists(query("groupId", groupId), Grouplist.class)) {
            //Add Checklist Obj Reference back to User's checklists attribute
            mongoTemplate.findAndModify(
                    query("userId", userId), 
                    pushUpdate("checklists", checklist), 
                    Student.class);

            return mongoTemplate.findAndModify(
                query("groupId", groupId), 
                pullUpdate("checklists", checklist), 
                options(true, true), 
                Grouplist.class);
        } else {
            throw new ApiRequestException("Grouplist You Wanted to Modify Doesn't Exist");
        }
    }

    //Delete Grouplist | Void
    public void deleteGrouplist(String userId, String groupId, boolean deleteAll) {
        Grouplist grouplist = Optional.ofNullable(
                mongoTemplate.findOne(
                    query("groupId", groupId), 
                    Grouplist.class))
            .orElseThrow(() -> new ApiRequestException("Grouplist You Wanted to Delete Doesn't Exist"));

        List<Checklist> checklists = grouplist.getChecklists(); //Checklist Under Grouplist

        if (deleteAll) {
            //Deleting Completely All Checklist Found within Deleted Grouplist
            for(Checklist list : checklists) {
                mongoTemplate.remove(
                        query("listId", list.getListId()), 
                        Checklist.class);
            }
        } else {
            //Move Checklist Found within Deleted Grouplist to User's Checklists attribute
            for(Checklist list : checklists) {
                mongoTemplate.findAndModify(
                        query("userId", userId), 
                        pushUpdate("checklists", list), 
                        Student.class);
            }
        }

        //Delete Grouplist
        mongoTemplate.remove(
                query("groupId", groupId), 
                Grouplist.class);
    }
}
