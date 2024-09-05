package com.jala.university.api.controllers;

import com.jala.university.core.models.UserModel;
import com.jala.university.core.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping()
    public UserModel createUser(@RequestBody UserModel user){
        return userService.createUser(user);
    }
}
