package com.academicdashboard.backend.professor;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.academicdashboard.backend.user.UserType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Document(collection = "professor")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Professor extends UserType {

    //Professor Specific Information
    private String department;
    private String academicRole; //View Google Doc for all options
    private int apptYear; //Appointed Year
    private String officeBuilding;
    private String officeRoom;
    private String researchArea;
    private List<OfficeHrs> officeHrs;

}
