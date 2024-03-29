package com.academicdashboard.backend.checklist;

import java.util.List;

import org.bson.types.ObjectId; //MongoDB ObjectId (AutoGenerated)
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "checklist")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Checklist {

    @Id
    private ObjectId id; //MongoDB ObjectId
    
    //CheckList's Details
    private String listId; //Used for Queries (JNanoId)
    private String title;

    @DocumentReference
    private List<Checkpoint> checkpoints;
}
