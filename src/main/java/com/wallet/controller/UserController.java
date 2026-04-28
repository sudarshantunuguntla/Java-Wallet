package com.wallet.controller;

import com.wallet.model.User;
import com.wallet.service.UserService;
import com.wallet.dto.UserResponseDTO;
import com.wallet.dto.LoginRequestDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController{
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public String login(@RequestBody LoginRequestDTO request) {
        return userService.login(request.getEmail(), request.getPassword());
    }
    @PostMapping
    public UserResponseDTO createUser(@Valid @RequestBody User user){
        return userService.createUser(user);
    }
}