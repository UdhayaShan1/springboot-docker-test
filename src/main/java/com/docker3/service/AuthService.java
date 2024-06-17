package com.docker3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.docker3.exception.auth.LoginFailedException;
import com.docker3.exception.auth.UserAlreadyExistsException;
import com.docker3.model.User;
import com.docker3.repository.UserRepository;
import com.docker3.util.PasswordEncoder;

import lombok.extern.java.Log;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerUser(User user) throws UserAlreadyExistsException {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new UserAlreadyExistsException("Username already taken");
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new UserAlreadyExistsException("Email already used");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void loginUser(User user) throws LoginFailedException {
        User retrievedUserFromDatabase = userRepository.findByUsername(user.getUsername());
        if (retrievedUserFromDatabase == null) {
            throw new LoginFailedException("User does not exist");
        }

        if (!passwordEncoder.matches(user.getPassword(), retrievedUserFromDatabase.getPassword())) {
            throw new LoginFailedException("Wrong password");
        }
    }
}
