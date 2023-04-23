package com.academicdashboard.backend.checklist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.academicdashboard.backend.student.Student;

@Service
public class ChecklistService {

    @Autowired
    private ChecklistRepository checklistRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public Checklist createChecklist(String title, String description, String firstName) {
        Checklist checklist = checklistRepository.insert(new Checklist(title, description)); 

        mongoTemplate.update(Student.class)
            .matching(Criteria.where("firstName").is(firstName))
            .apply(new Update().push("checklists").value(checklist))
            .first();

        return checklist;
    }

}
