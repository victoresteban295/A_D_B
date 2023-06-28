package com.academicdashboard.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        /* Configuring all the HTTP Security of our Application */
        http
            .csrf()
            .disable()
            .authorizeHttpRequests()
            .requestMatchers("/api/auth/**") //Whitelist: endpoints don't require authentication
            .permitAll()
            .anyRequest()
            .authenticated()
            .and()
            .sessionManagement() //Ensures Sever Holds No Session Info (STATELESS)
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and() //Provide AuthenticationProvider We Want to Use
            .authenticationProvider(authenticationProvider) //Add JwtAuthenticationFilter Before User..Filter
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
            
        return http.build();
    }
}
