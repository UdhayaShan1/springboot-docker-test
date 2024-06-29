package com.docker3.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.docker3.model.Users;
import com.docker3.service.AuthService;
import com.docker3.service.UserService;

@Controller
public class UserController {
    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;

    @GetMapping("/getusers")
    public ResponseEntity<List<Users>> getAllUsers() {
        try {
            List<Users> usersList = userService.getAllUsers();
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(usersList);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ArrayList<>());
        }
    }
}
