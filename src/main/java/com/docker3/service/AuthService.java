package com.docker3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.docker3.exception.auth.UserAlreadyExistsException;
import com.docker3.model.User;
import com.docker3.repository.UserRepository;
import com.docker3.util.PasswordEncoder;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean isUserRegistered(String username) {
        User user = userRepository.findByUsername(username);
        return user != null;
    }

    public void registerUser(User user) throws RuntimeException {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new UserAlreadyExistsException("Username already exists!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }



}
