package com.academicdashboard.backend.checklist;

import java.util.List;
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
public class GrouplistService {

    @Autowired
    private GrouplistRepository repository;

    @Autowired
    private ChecklistRepository checklistRepo;

    @Autowired
    private MongoTemplate mongoTemplate;

    private Random random = new Random();
    private char[] alphabet = {'a','b','c','d','e','1','2','3','5'};

    //Create New Grouplist | Returns Grouplist Created
    public Grouplist createGrouplist(String userId, String title) {
        String groupId = NanoIdUtils.randomNanoId(random, alphabet, 5);
        Grouplist grouplist = repository.insert(new Grouplist(groupId, title));

        mongoTemplate.update(Student.class)
            .matching(Criteria.where("userId").is(userId))
            .apply(new Update().push("grouplists").value(grouplist))
            .first();

        return grouplist;
    }

    //Modify Existing Grouplist | Returns Modified Grouplist
    public Grouplist modifyGrouplist(String groupId, String newTitle) {
        Query query = new Query().addCriteria(Criteria.where("groupId").is(groupId));
        Update updateDef = new Update().set("title", newTitle);
        FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true).upsert(true); //Returns Modified Value

        return mongoTemplate.findAndModify(query, updateDef, options, Grouplist.class);
    }

    //Add New Checklist to Grouplist | Returns Grouplist
    public Grouplist addNewToGrouplist(String groupId, String listTitle) {
        String listId = NanoIdUtils.randomNanoId(random, alphabet, 5);
        Checklist checklist = checklistRepo.insert(new Checklist(listId, listTitle)); //Make sure no Duplicates

        Query query = new Query().addCriteria(Criteria.where("groupId").is(groupId));
        Update updateDef = new Update().push("checklists").value(checklist);
        FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true).upsert(true); //Returns Modified Value

        return mongoTemplate.findAndModify(query, updateDef, options, Grouplist.class);
    }

    //Add Existing Checklist to Grouplist | Returns Grouplist
    public Grouplist addExistToGrouplist(String userId, String groupId, String listId) {

        //Remove Checklist Obj Reference from the User's checklists attribute
        Checklist checklist = checklistRepo.findChecklistByListId(listId).get();
        Query studQuery = new Query().addCriteria(Criteria.where("userId").is(userId));
        Update studUpdate = new Update().pull("checklists", checklist);
        mongoTemplate.findAndModify(studQuery, studUpdate, Student.class);

        //Add Same Checklist Obj Reference to Grouplist's checklists attribute
        Query grpQuery = new Query().addCriteria(Criteria.where("groupId").is(groupId));
        Update grpUpdate = new Update().push("checklists").value(checklist);
        FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true).upsert(true); //Returns Modified Value

        return mongoTemplate.findAndModify(grpQuery, grpUpdate, options, Grouplist.class); //Return Update Grouplist
    }

    //Remove Existing Checklist From Grouplist | Returns Modified Grouplist
    public Grouplist removefromGrouplist(String userId, String groupId, String listId) {

        //Add Checklist Obj Reference back to User's checklists attribute
        Checklist checklist = checklistRepo.findChecklistByListId(listId).get();
        Query studQuery = new Query().addCriteria(Criteria.where("userId").is(userId));
        Update studUpdate = new Update().push("checklists").value(checklist);
        mongoTemplate.findAndModify(studQuery, studUpdate, Student.class);

        //Remove Checklist Obj Reference from Grouplist's checklists attribute
        Query grpQuery = new Query().addCriteria(Criteria.where("groupId").is(groupId));
        Update grpUpdate = new Update().pull("checklists", checklist);
        FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true).upsert(true); //Returns Modified Value

        return mongoTemplate.findAndModify(grpQuery, grpUpdate, options, Grouplist.class); //Return Update Grouplist
    }

    //Delete Grouplist | Void
    public void deleteGrouplist(String userId, String groupId, boolean deleteAll) {

        Grouplist grouplist = repository.findGrouplistByListId(groupId).get();
        List<Checklist> checklists = grouplist.getChecklists();
        
        if (deleteAll) {
            //Deleting Completely All Checklist Found within Deleted Grouplist
            for(Checklist list : checklists) {
                Query query = new Query().addCriteria(Criteria.where("listId").is(list.getListId()));
                mongoTemplate.remove(query, Checklist.class);
            }
        } else {
            //Move Checklist Found within Deleted Grouplist to User's Checklists attribute
            for(Checklist list : checklists) {
                Query query = new Query().addCriteria(Criteria.where("userId").is(userId));
                Update updateDef = new Update().push("checklists").value(list);
                mongoTemplate.findAndModify(query, updateDef, Student.class);
            }
        }

        Query query = new Query().addCriteria(Criteria.where("groupId").is(groupId));

        mongoTemplate.remove(query, Grouplist.class);
    }

}
