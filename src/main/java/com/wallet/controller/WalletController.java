package com.wallet.controller;

import com.wallet.model.Wallet;
import com.wallet.repository.WalletRepository;
import com.wallet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallet")
public class WalletController{
    @Autowired
    private WalletRepository walletRepository;
    
    @Autowired
    private UserService userService;

    @GetMapping("/{userId}")
    public Wallet getWalletByUserId(@PathVariable Long userId){
        return walletRepository.findByUserId(userId);
    }
    @PostMapping("/add")
    public Wallet addMoney(@RequestParam Long userId, @RequestParam Double amount){

        return userService.addMoney(userId, amount);
    }
    @PostMapping("/transfer")
    public String transferMoney(@RequestParam Long senderId, @RequestParam Long receiverId, @RequestParam Double amount){
        return userService.transferMoney(senderId, receiverId, amount);
        
    }
}