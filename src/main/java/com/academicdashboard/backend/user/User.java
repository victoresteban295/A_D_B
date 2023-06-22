package com.academicdashboard.backend.user;

import java.util.Collection;
import java.util.List;

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
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

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
    private List<ReminderList> reminderList;



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
