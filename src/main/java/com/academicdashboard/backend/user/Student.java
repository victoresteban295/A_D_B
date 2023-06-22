package com.academicdashboard.backend.user;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import com.academicdashboard.backend.calendar.Calendar;
import com.academicdashboard.backend.checklist.Checklist;
import com.academicdashboard.backend.checklist.Grouplist;
import com.academicdashboard.backend.course.Course;
import com.academicdashboard.backend.reminder.ReminderList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Student extends User {

    @Id
    private ObjectId id; //MongoDB ObjectId

    //Student Specific Information
    private String gradeLvl;
    private String major;
    private String minor;
    private String concentration;

    //Constructor without ObjectId for PUT Endpoint Methods 
    public Student(
            String userId,
            String firstName,
            String middleName,
            String lastName,
            String birthMonth,
            int birthDay,
            int birthYear,
            String schoolName,
            String schoolId,
            String gradeLvl,
            String major,
            String minor,
            String concentration,
            String username,
            String email,
            String password,
            String phone,
            Role role,
            List<Course> courses,
            List<Calendar> calendars,
            List<Grouplist> grouplists,
            List<Checklist> checklists,
            List<ReminderList> reminderLists
        ) {

        super(
                userId,
                firstName, 
                middleName,
                lastName, 
                birthMonth,
                birthDay,
                birthYear,
                schoolName,
                schoolId,
                username,
                email,
                password,
                phone,
                role,
                courses,
                calendars,
                grouplists,
                checklists,
                reminderLists);

        this.gradeLvl = gradeLvl;
        this.major = major;
        this.minor = minor;
        this.concentration = concentration;
    }

}
