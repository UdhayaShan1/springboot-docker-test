package com.docker3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.servlet.http.HttpSession;

import com.docker3.exception.auth.LoginFailedException;
import com.docker3.exception.auth.UserAlreadyExistsException;
import com.docker3.model.User;
import com.docker3.service.AuthService;

@Controller
public class AuthController {
    @Autowired
    private AuthService authService;


    @GetMapping
    public String landingPage() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String homePage(HttpSession session) {
        if (session.getAttribute("user") != null) {
            return "home";
        }
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage(HttpSession session) {
        if (session.getAttribute("user") != null) {
            return "home";
        }
        return "login";
    }

    @PostMapping("/login")
    public ResponseEntity<String> beginUserLogin(@RequestBody User user , HttpSession session) {

        try {
            User retrievedUser = authService.loginUser(user);
            session.setAttribute("user", retrievedUser);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("Logged in");
        } catch (LoginFailedException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/logout")
    public String logoutUser(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }


    @GetMapping("/register")
    public String registerPage(HttpSession session) {
        if (session.getAttribute("user") != null) {
            return "home";
        }
        return "register";
    }

    @PostMapping("/register")
    public ResponseEntity<String> beginUserRegistration(@RequestBody User newUser) {
        try {
            System.out.println("TEST" + newUser);

            authService.registerUser(newUser);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(newUser.getUsername() + " has been registered!");
        } catch (UserAlreadyExistsException exception) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(exception.getMessage());
        }
    }

    @GetMapping("/order")
    public String orderPage(HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        return "orders";
    }




}
