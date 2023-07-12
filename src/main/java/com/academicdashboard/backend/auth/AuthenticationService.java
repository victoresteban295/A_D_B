package com.academicdashboard.backend.auth;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.academicdashboard.backend.config.JwtService;
import com.academicdashboard.backend.professor.Professor;
import com.academicdashboard.backend.student.Student;
import com.academicdashboard.backend.token.Token;
import com.academicdashboard.backend.token.TokenRepository;
import com.academicdashboard.backend.user.Role;
import com.academicdashboard.backend.user.User;
import com.academicdashboard.backend.user.UserRepository;
import com.academicdashboard.backend.user.UserType;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;

import lombok.RequiredArgsConstructor;

@Service
// @RequiredArgsConstructor
public class AuthenticationService {

    // private final UserRepository userRepository;
    // private final TokenRepository tokenRepository;
    // private final MongoTemplate mongoTemplate;
    // private final PasswordEncoder passwordEncoder;
    // private final JwtService jwtService;
    // private final AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    /* Register New User */
    public AuthenticationResponse register(RegisterRequest request) {

        UserType profile;
        Role role;
        String userId = NanoIdUtils.randomNanoId();

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
            .userId(userId)
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
        saveUserToken(userId, jwtToken); //Build & Store Token Instance 

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
        var jwtToken = jwtService.generateToken(user); //Generate JWT
        revokeAllUserTokens(user.getUserId()); //Expire & Revoke All Old Tokens
        saveUserToken(user.getUserId(), jwtToken); //Save New Token to Repo

        return AuthenticationResponse.builder()
            .username(request.getUsername())
            .token(jwtToken)
            .build();
    }

    /*************** Private Methods ***************/
    private void saveUserToken(String userId, String jwt) {
        //Create a Token Instance
        var token = Token.builder()
            .userId(userId)
            .token(jwt)
            .revoked(false)
            .expired(false)
            .build();

        tokenRepository.save(token); //Save Token to Repository
    }

    private void revokeAllUserTokens(String userId) {
        //Create a Query for User's Existing Tokens That Aren't Expired nor Revoked
        Query query = new Query(new Criteria()
                .andOperator(
                    new Criteria().orOperator(
                        Criteria.where("revoked").is(false), 
                        Criteria.where("expired").is(false)), 
                    Criteria.where("userId").is(userId)));

        //Extracted Tokens (the match query) from Repository 
        var validUserTokens = mongoTemplate.find(query, Token.class);
        if(validUserTokens.isEmpty()) return; //Return if not tokens 

        //Update Each Individual User Token to be Expired & Revoked
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });

        tokenRepository.saveAll(validUserTokens); //Add Updates to Repo
    }

}
