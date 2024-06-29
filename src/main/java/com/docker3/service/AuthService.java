package com.docker3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.docker3.exception.auth.LoginFailedException;
import com.docker3.exception.auth.UserAlreadyExistsException;
import com.docker3.model.Users;
import com.docker3.repository.UserRepository;
import com.docker3.util.PasswordEncoder;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerUser(Users users) throws UserAlreadyExistsException {
        if (userRepository.findByUsername(users.getUsername()) != null) {
            throw new UserAlreadyExistsException("Username already taken");
        }
        if (userRepository.findByEmail(users.getEmail()) != null) {
            throw new UserAlreadyExistsException("Email already used");
        }

        users.setPassword(passwordEncoder.encode(users.getPassword()));
        userRepository.save(users);
    }

    public Users loginUser(Users users) throws LoginFailedException {
        Users retrievedUsersFromDatabase = userRepository.findByUsername(users.getUsername());
        if (retrievedUsersFromDatabase == null) {
            throw new LoginFailedException("Users does not exist");
        }

        if (!passwordEncoder.matches(users.getPassword(), retrievedUsersFromDatabase.getPassword())) {
            throw new LoginFailedException("Wrong password");
        }
        return retrievedUsersFromDatabase;
    }
}
