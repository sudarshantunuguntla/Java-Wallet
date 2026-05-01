package com.wallet.controller;

import com.wallet.model.Wallet;
import com.wallet.repository.WalletRepository;
import com.wallet.service.WalletService;
import com.wallet.dto.ApiResponse;
import com.wallet.exception.CustomException;
import org.springframework.data.domain.Page;
import com.wallet.dto.TransactionResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallet")
public class WalletController{
    
    @Autowired
    private WalletService walletService;

    @GetMapping("/{userId}")
    public ApiResponse<Wallet> getWalletByUserId(@PathVariable Long userId,HttpServletRequest request) {

        String email = (String) request.getAttribute("userEmail");

        Wallet wallet= walletService.getWallet(userId,email);
        return new ApiResponse<>(true, "Wallet Fetched", wallet);
    }
    @PostMapping("/add")
    public ApiResponse<Wallet> addMoney(@RequestParam Double amount,HttpServletRequest request){
        String email = (String) request.getAttribute("userEmail");
        Wallet wallet = walletService.addMoney(amount,email);
        return new ApiResponse<>(true, "Money added successfully", wallet);
    }
    @PostMapping("/transfer")
    public ApiResponse<String> transferMoney(@RequestParam Long receiverId, @RequestParam Double amount,HttpServletRequest request){
        String email = (String) request.getAttribute("userEmail");
        String result= walletService.transferMoney(receiverId, amount, email);
        return new ApiResponse<>(true,result,null);
        
    }

    @GetMapping("/transactions")
    public ApiResponse<Page<TransactionResponseDTO>> getTransactions(
            @RequestParam int page,
            @RequestParam int size,
            HttpServletRequest request) {

        String email = (String) request.getAttribute("userEmail");

        Page<TransactionResponseDTO> transactions =
                walletService.getTransactions(email, page, size);

        return new ApiResponse<>(true, "Transaction history", transactions);
    }
}