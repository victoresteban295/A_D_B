package com.academicdashboard.backend.checklist;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GrouplistRepository extends MongoRepository<Grouplist, ObjectId> {

}
