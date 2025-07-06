package com.flipkart.raghav.controller;

import com.flipkart.raghav.model.Users;
import com.flipkart.raghav.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Users register(@RequestBody Users user){
        return userService.register(user);
    }
    @PostMapping("/login")
    public String login(@RequestBody Users user){
        return userService.verify(user);
    }
}
