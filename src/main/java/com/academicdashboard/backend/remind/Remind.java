package com.academicdashboard.backend.remind;

import org.bson.types.ObjectId; //MongoDB ObjectId (AutoGenerated)
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "remind")
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Remind {

    @Id
    private ObjectId id; //MongoDB ObjectId

    //Reminder Details
    private String Reminder; //Actual Reminder
    private String remindDate;
    private String remindTime;

    private boolean isComplete;
    private boolean isPastDue;
}
