package com.wallet.controller;

import com.wallet.model.User;
import com.wallet.service.UserService;
import com.wallet.dto.UserResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController{
    @Autowired
    private UserService userService;

    @PostMapping
    public UserResponseDTO createUser(@RequestBody User user){
        return userService.createUser(user);
    }
}