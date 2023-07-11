package com.academicdashboard.backend.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import com.academicdashboard.backend.token.TokenRepository;
import com.academicdashboard.backend.user.UserDetailsServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

// @Component
// @RequiredArgsConstructor
@Service
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // private final JwtService jwtService;
    // private final UserDetailsService userDetailsService;
    // private final TokenRepository tokenRepository;

    @Autowired
    private JwtService jwtService;

    // @Autowired
    // private UserDetailsService userDetailsService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request, 
            @NonNull HttpServletResponse response, 
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        /* No JWT Found in Header of Request */
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7); //Extracts JWT (Removes "Bearer ")
        username = jwtService.extractUsername(jwt); //Extract username from JWT

        /* User Is Not Yet Authenticated */
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            //Obtain UserDetails Obj using Username extracted from JWT
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            var isTokenValid = tokenRepository.findByToken(jwt)
                .map(t -> !t.isExpired() && !t.isRevoked())
                .orElse(false);

            //Check if JWT is Valid
            //If So, Update Security Context Using UsernamePasswordAuthenticationToken
            if(jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
                //Create UsernamePasswordAuthenticationToken
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, 
                        null, //credentials 
                        userDetails.getAuthorities());

                //Add More Details to Token Using Request
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));

                //Update Security Context
                SecurityContextHolder.getContext().setAuthentication(authToken); 
            }
        }
        filterChain.doFilter(request, response); //Continue to Next Filter in FilterChain
    }
}
