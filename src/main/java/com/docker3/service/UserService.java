package com.docker3.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.docker3.exception.user.UserNotFoundException;
import com.docker3.model.Users;
import com.docker3.repository.UserRepository;
import com.docker3.util.PasswordEncoder;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    public Users retrieveUser(Long id) throws UserNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with specified ID does not exist"));
    }

    public void updateUserFields(Users userInSession, Users refUser) {
        if (!refUser.getUsername().isEmpty()) {
            userInSession.setUsername(refUser.getUsername());
        }

        if (!refUser.getEmail().isEmpty()) {
            userInSession.setEmail(refUser.getEmail());
        }
        userRepository.save(userInSession);
    }

    public void updatePasswordOfLoggedInUser(Users loggedInUser, String password) {
        loggedInUser.setPassword(passwordEncoder.encode(password));
        userRepository.save(loggedInUser);
    }

}
