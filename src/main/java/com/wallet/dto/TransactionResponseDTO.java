package com.wallet.dto;

import lombok.Data;

@Data
public class TransactionResponseDTO {

    private Double amount;
    private String type;
    private Long senderId;
    private Long receiverId;
    private String status;
    private String createdAt;
}