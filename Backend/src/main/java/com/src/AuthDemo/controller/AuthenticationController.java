package com.src.AuthDemo.controller;

import com.src.AuthDemo.model.Role;
import com.src.AuthDemo.model.User;
import com.src.AuthDemo.model.dto.AuthenticationDto;
import com.src.AuthDemo.security.jwt.JwtTokenProvider;
import com.src.AuthDemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/auth/")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserService userService;

    @PostMapping("login")
    public ResponseEntity login(@RequestBody AuthenticationDto requestDto) {
        try {
            String username = requestDto.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, requestDto.getPassword()));
            User user = userService.getUserByName(username);
            if (user != null) {
                List<Role> userRoles= new ArrayList<>();
                user.getRoles().forEach(role->{
                    userRoles.add(role);
                });
                String token = jwtTokenProvider.createToken(username, userRoles);
                Map<Object, Object> response = new HashMap<>();
                response.put("username", username);
                response.put("token", token);
                response.put("roles", user.getRoles());
                return ResponseEntity.ok(response);
            } else {
                throw new UsernameNotFoundException("User with username: " + username + " not found");
            }
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

}

