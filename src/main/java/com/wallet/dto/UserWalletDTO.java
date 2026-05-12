package com.wallet.dto;

public class UserWalletDTO {

    private Long userId;
    private Double balance;

    public UserWalletDTO(Long userId, Double balance) {
        this.userId = userId;
        this.balance = balance;
    }

    public Long getUserId() { return userId; }
    public Double getBalance() { return balance; }
}