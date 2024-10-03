package com.example.scheduling_system.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.scheduling_system.models.User;
import com.example.scheduling_system.payload.response.UserResponse;
import com.example.scheduling_system.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public User findByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
    }

    public UserResponse create(User user) throws RuntimeException {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("User already exits!");
        }

        User newUser = userRepository.save(user);
        return new UserResponse(newUser.getId(), newUser.getUsername(), newUser.getRole());
    }
}
