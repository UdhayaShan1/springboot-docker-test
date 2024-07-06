package com.docker3.exception.user;

import com.docker3.service.UserService;

public class UserNotFoundException extends Exception {

    public UserNotFoundException(String error) {
        super(error);
    }
}
