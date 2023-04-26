package com.academicdashboard.backend.checklist;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Service;

import com.academicdashboard.backend.student.Student;

@Service
public class ChecklistService {

    @Autowired
    private ChecklistRepository repository;

    @Autowired
    private MongoTemplate mongoTemplate;

    //Called When Checklist is 1st Created
    public Checklist createChecklist(String title, String firstName) {
        Checklist checklist = repository.insert(new Checklist(title)); 

        mongoTemplate.update(Student.class)
            .matching(Criteria.where("firstName").is(firstName))
            .apply(new Update().push("checklists").value(checklist))
            .first();

        return checklist;
    }

    //DONT FORGET TO LIMIT CHECKPOINT TO 50 PER CHECKLIST
    //Add Checkpoint to an existing Checklist
    public Optional<Checklist> createCheckpoint(ObjectId listId, String content) {
        Checkpoint checkpoint = new Checkpoint(content, false);

        mongoTemplate.update(Checklist.class)
            .matching(Criteria.where("id").is(listId))
            .apply(new Update().push("toComplete").value(checkpoint));

        return repository.findById(listId);
    }
}
