package com.academicdashboard.backend.user;

import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.academicdashboard.backend.checklist.Checklist;
import com.academicdashboard.backend.checklist.Grouplist;
import com.academicdashboard.backend.course.Course;
import com.academicdashboard.backend.reminder.ReminderList;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "user")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    private ObjectId id; //MongoDB ObjectId
    private String userId; //Public Id (JNanoId)

    //User Information
    private String firstname;
    private String lastname;

    //Account Information
    private String profileType; //STUDENT || PROFESSOR
    private String email;
    private String phone;
    private String username;
    private String password;

    @DocumentReference
    private Set<Role> authorities;

    //Academic Institution Information
    private String schoolName;
    private String schoolId;

    @DocumentReference
    private Profile profile;

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

    /********** UserDetails Interface Override Methods ***********/
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
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
