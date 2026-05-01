package com.wallet.controller;

import com.wallet.model.User;
import com.wallet.service.UserService;
import com.wallet.dto.UserResponseDTO;
import com.wallet.dto.LoginRequestDTO;
import com.wallet.dto.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController{
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ApiResponse<String> login(@RequestBody LoginRequestDTO request) {
        String Token = userService.login(request.getEmail(), request.getPassword());
        return new ApiResponse<>(true, "Login successful", Token);
    }
    @PostMapping
    public ApiResponse<UserResponseDTO> createUser(@Valid @RequestBody User user){
        UserResponseDTO user_response = userService.createUser(user);
        return new ApiResponse<>(true, "User created successfully", user_response);
    }
}