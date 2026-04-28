package com.wallet.dto;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String email;
    private String password;
}