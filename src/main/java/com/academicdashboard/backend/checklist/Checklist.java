package com.academicdashboard.backend.checklist;

import java.util.List;

import org.bson.types.ObjectId; //MongoDB ObjectId (AutoGenerated)
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "checklist")
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Checklist {

    @Id
    private ObjectId id; //MongoDB ObjectId
    
    //CheckList's Details
    private String title;
    private String description;

    //All Checkpoints under this checklist
    private List<ObjectId> checkPoints;
}
