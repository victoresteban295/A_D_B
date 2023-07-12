package com.academicdashboard.backend.user;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserType {

    @Id
    private ObjectId id; //MongoDB ObjectId
    private String profileId; //Public Id (JNanoId)

    //Personal Information
    private String firstName;
    private String middleName;
    private String lastName;
    private String birthMonth; 
    private int birthDay;
    private int birthYear;

}
