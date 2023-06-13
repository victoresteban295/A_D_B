package com.academicdashboard.backend.checklist;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "checkpoint")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Checkpoint {
    
    @Id
    private ObjectId id;

    
    String pointId;
    String content;
    @JsonProperty(value = "isComplete")
    boolean isComplete;
    @JsonProperty(value = "isSubpoint")
    boolean isSubpoint;
    List<Checkpoint> subCheckpoints;

    //Constructor: Checkpoint is Created
    public Checkpoint(
            String pointId,
            String content,
            boolean isComplete,
            boolean isSubpoint
        ) {
        
        this.pointId = pointId;
        this.content = content;
        this.isComplete = isComplete;
        this.isSubpoint = isSubpoint;
        this.subCheckpoints = new ArrayList<>();
    }
}
