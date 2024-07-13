package com.docker3.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

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


    @PutMapping("/user/updatepassword")
    public ResponseEntity<Void> updatePasswordOfLoggedInUser(@RequestBody Map<String, String> payload, HttpSession httpSession) {
        String password = payload.get("password");
        System.out.println(password);

        Users user = (Users) httpSession.getAttribute("user");
        System.out.println(user);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        userService.updatePasswordOfLoggedInUser(user, password);
        // Force user to log in again
        httpSession.invalidate();
        return ResponseEntity.ok().build();
    }

}
