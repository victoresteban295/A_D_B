package com.academicdashboard.backend.student;

import java.util.List;

import org.bson.types.ObjectId; //MongoDB ObjectId (AutoGenerated)
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import com.academicdashboard.backend.calendar.Calendar;
import com.academicdashboard.backend.checklist.Checklist;
import com.academicdashboard.backend.checklist.Grouplist;
import com.academicdashboard.backend.course.Course;
import com.academicdashboard.backend.reminder.ReminderList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "student")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    @Id
    private ObjectId id; //MongoDB ObjectId

    private String userId; //Public Id (JNanoId)

    //Student's Personal Info
    private String firstName;
    private String lastName;
    private String birthMonth; 
    private int birthDay;
    private int birthYear;

    //Student's School Info
    private String schoolName;
    private String gradeLevel; 
    private String major;
    private String minor;
    private String concentration;

    //Student's Account Info
    private String email;
    private String password;
    private String phone;

    //Data Relationships
    @DocumentReference
    private List<Course> courses;

    @DocumentReference
    private List<Calendar> calendars;

    @DocumentReference
    private List<Grouplist> grouplists;

    @DocumentReference
    private List<Checklist> checklists;

    @DocumentReference
    private List<ReminderList> reminderList;

    //Constructor without ObjectId for PUT Endpoint Methods 
    public Student(
            String userId,
            String firstName,
            String lastName,
            String birthMonth,
            int birthDay,
            int birthYear,
            String schoolName,
            String gradeLevel,
            String major,
            String minor,
            String concentration,
            String email,
            String password,
            String phone
        ) {

        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthMonth = birthMonth;
        this.birthDay = birthDay;
        this.birthYear = birthYear;
        this.schoolName = schoolName;
        this.gradeLevel = gradeLevel;
        this.major = major;
        this.minor = minor;
        this.concentration = concentration;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }
}
