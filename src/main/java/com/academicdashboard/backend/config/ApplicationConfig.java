package com.academicdashboard.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.academicdashboard.backend.user.StudentRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final StudentRepository studentRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> studentRepository.findStudentByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Username Not Valid"));
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        //Create Type of AuthenticationProvider We're Implementing
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(); 

        //Provide the AuthenticationProvider The UserDetailsService to Use 
        authProvider.setUserDetailsService(userDetailsService()); //Passing Bean Above

        //Provide the AuthenticationProvider The PasswordEncoder to Use
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        //AuthenticationManager is responsible for the actual authentication
        //inject Spring's Default AuthenticationConfiguration
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
