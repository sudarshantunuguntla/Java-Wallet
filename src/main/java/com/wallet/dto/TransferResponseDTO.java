package com.wallet.dto;

public class TransferResponseDTO{
    private UserWalletDTO sender;
    private UserWalletDTO receiver;

    public TransferResponseDTO(UserWalletDTO sender, UserWalletDTO receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }

    public UserWalletDTO getSender() { return sender; }
    public UserWalletDTO getReceiver() { return receiver; }
}