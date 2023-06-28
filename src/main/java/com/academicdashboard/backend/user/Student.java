package com.academicdashboard.backend.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.academicdashboard.backend.calendar.Calendar;
import com.academicdashboard.backend.checklist.Checklist;
import com.academicdashboard.backend.checklist.Grouplist;
import com.academicdashboard.backend.course.Course;
import com.academicdashboard.backend.reminder.ReminderList;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "student")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Student implements UserDetails {

    @Id
    private ObjectId id; //MongoDB ObjectId

    private String userId; //Public Id (JNanoId)

    //Personal Information
    private String firstName;
    private String middleName;
    private String lastName;
    private String birthMonth; 
    private int birthDay;
    private int birthYear;

    //Academic Institution Information
    private String schoolName;
    private String schoolId;

    //Account Information
    private String username;
    private String email;
    private String password;
    private String phone;

    //Student Specific Information
    private String gradeLvl;
    private String major;
    private String minor;
    private String concentration;

    private Role role;

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
    private List<ReminderList> reminderLists;


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
            String username,
            String email,
            String password,
            String phone,
            String gradeLvl,
            String major,
            String minor,
            String concentration,
            Role role
        ) {

        this.userId = userId;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.birthMonth = birthMonth;
        this.birthDay = birthDay;
        this.birthYear = birthYear;
        this.schoolName = schoolName;
        this.schoolId = schoolId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phone = phone; 
        this.gradeLvl = gradeLvl;
        this.major = major;
        this.minor = minor;
        this.concentration = concentration;
        this.role = role;
        this.courses = new ArrayList<>();
        this.calendars = new ArrayList<>();
        this.grouplists = new ArrayList<>();
        this.checklists = new ArrayList<>();
        this.reminderLists = new ArrayList<>();
    }

    /********** UserDetails Interface Override Methods ***********/
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
