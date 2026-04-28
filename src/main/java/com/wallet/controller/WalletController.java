package com.wallet.controller;

import com.wallet.model.Wallet;
import com.wallet.repository.WalletRepository;
import com.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallet")
public class WalletController{
    
    @Autowired
    private WalletService walletService;

    @GetMapping("/{userId}")
    public Wallet getWalletByUserId(@PathVariable Long userId){
        return walletService.getWallet(userId);
    }
    @PostMapping("/add")
    public Wallet addMoney(@RequestParam Long userId, @RequestParam Double amount){

        return walletService.addMoney(userId, amount);
    }
    @PostMapping("/transfer")
    public String transferMoney(@RequestParam Long senderId, @RequestParam Long receiverId, @RequestParam Double amount){
        return walletService.transferMoney(senderId, receiverId, amount);
        
    }
}