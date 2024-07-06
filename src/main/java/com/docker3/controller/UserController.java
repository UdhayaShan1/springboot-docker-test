package com.docker3.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.docker3.model.Users;
import com.docker3.service.AuthService;
import com.docker3.service.UserService;

import jakarta.servlet.http.HttpSession;

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

    //Web controller stuff

    @GetMapping("/user/display")
    public ResponseEntity<Users> displayLoggedinUser(HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        return ResponseEntity.ok(user);
    }

    @PutMapping("/user/update")
    public ResponseEntity<Void> updateLoggedinUser(@RequestBody Users user, HttpSession session) {
        Users userInSessionContext =  (Users) session.getAttribute("user");
        userService.updateUserFields(userInSessionContext, user);
        return ResponseEntity.ok().build();
    }
}
