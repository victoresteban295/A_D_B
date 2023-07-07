package com.academicdashboard.backend.auth;

import java.util.ArrayList;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.academicdashboard.backend.config.JwtService;
import com.academicdashboard.backend.professor.Professor;
import com.academicdashboard.backend.student.Student;
import com.academicdashboard.backend.user.Profile;
import com.academicdashboard.backend.user.Role;
import com.academicdashboard.backend.user.User;
import com.academicdashboard.backend.user.UserRepository;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /* Register New User */
    public AuthenticationResponse register(RegisterRequest request) {

        Profile profile;
        Role role;

        if(request.getProfileType() == "STUDENT") {
            profile = new Student();
            profile.setFirstName(request.getFirstName());
            profile.setLastName(request.getLastName());
            role = Role.STUDENT;
        } else {
            profile = new Professor();
            profile.setFirstName(request.getFirstName());
            profile.setLastName(request.getLastName());
            role = Role.PROF;
        }

        //Create New User Using Builder
        var user = User.builder()
            .userId(NanoIdUtils.randomNanoId())
            .firstname(request.getFirstName())
            .lastname(request.getLastName())
            .profileType(request.getProfileType())
            .email(request.getEmail())
            .username(request.getUsername())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(role)
            .schoolName(request.getSchoolName())
            .schoolId(request.getSchoolId())
            .profile(profile)
            .courses(new ArrayList<>())
            .calendars(new ArrayList<>())
            .grouplists(new ArrayList<>())
            .checklists(new ArrayList<>())
            .reminderLists(new ArrayList<>())
            .build();

        userRepository.save(user); //Save New User to Repository

        //Create new JWT Token for Response
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
            .username(request.getUsername())
            .token(jwtToken)
            .build();
    }

    /* Authenticate Existing User */
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        //Use the AuthenticationManager's authenticate() method 
        //to authenticate users based on the username & password
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getUsername(), 
                    request.getPassword()));

        //Pull Student User From Repository
        var user = userRepository.findUserByUsername(request.getUsername())
            .orElseThrow(() -> new UsernameNotFoundException("User Not Found!"));

        //Create new JWT Token for Response
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
            .username(request.getUsername())
            .token(jwtToken)
            .build();
    }
}
