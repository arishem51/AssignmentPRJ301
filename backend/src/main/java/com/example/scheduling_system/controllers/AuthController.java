package com.example.scheduling_system.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.scheduling_system.dto.payload.request.UserRequest;
import com.example.scheduling_system.dto.payload.response.BodyResponse;
import com.example.scheduling_system.enums.UserRole;
import com.example.scheduling_system.jwt.JwtAuthenProvider;
import com.example.scheduling_system.models.User;
import com.example.scheduling_system.services.UserService;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtAuthenProvider jwtAuthenProvider;

    private ResponseEntity<?> createUser(@RequestBody UserRequest request, UserRole role) {
        try {
            User user = new User(request.username(), passwordEncoder.encode(request.password()), role);
            userService.create(user);
            return ResponseEntity.ok().body(new BodyResponse<>("Success!"));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new BodyResponse<>(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password(), null));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        var response = new HashMap<>();

        response.put("username", authentication.getName());
        response.put("role",
                authentication.getAuthorities().stream().findFirst().map(GrantedAuthority::getAuthority).orElse(null));
        response.put("token", jwtAuthenProvider.createToken(authentication.getName()));

        return ResponseEntity.ok().body(new BodyResponse<>("Success!", response));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody UserRequest request) {
        return this.createUser(request, UserRole.USER);
    }

    @PostMapping("/create/admin")
    public ResponseEntity<?> createAdminUser(@RequestBody UserRequest request) {
        return this.createUser(request, UserRole.ADMIN);
    }

}
