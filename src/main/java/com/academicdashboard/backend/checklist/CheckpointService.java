package com.academicdashboard.backend.checklist;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class CheckpointService {

    @Autowired
    private CheckpointRepository checkpointRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public Checkpoint createCheckpoint(String content, ObjectId checklistId) {
        Checkpoint checkpoint = checkpointRepository.insert(new Checkpoint(content, false));

        mongoTemplate.update(Checklist.class)
            .matching(Criteria.where("checklistId").is("id"))
            .apply(new Update().push("checkpoints").value(checkpoint))
            .first();

        return checkpoint;
    }
}
