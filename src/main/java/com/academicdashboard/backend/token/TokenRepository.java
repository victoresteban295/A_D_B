package com.academicdashboard.backend.token;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TokenRepository extends MongoRepository<Token, ObjectId> {

    Optional<Token> findByToken(String token);
}
