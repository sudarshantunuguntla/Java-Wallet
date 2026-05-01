package com.wallet.service;

import com.wallet.model.Wallet;
import com.wallet.model.User;
import com.wallet.repository.WalletRepository;
import com.wallet.service.UserService;
import com.wallet.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WalletService {
    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserService userService;

    public Wallet getWallet(Long userId,String email) {
        Wallet wallet=walletRepository.findByUserId(userId);
        User user = userService.getUserByEmail(email);
        if (user == null) {
            throw new CustomException("User not found");
        }
        if (wallet == null) {
            throw new CustomException("Wallet not found for user: " + userId);
        }
        if (!user.getId().equals(userId)) {
            throw new CustomException("Unauthorized access");
        }
        return wallet;
    }

    @Transactional
    public Wallet addMoney(Double amount,String email) {

        User user = userService.getUserByEmail(email);
        Wallet wallet = walletRepository.findByUserIdForUpdate(user.getId());

        if (wallet == null) {
            throw new CustomException("Wallet not found");
        }

        if (amount <= 0) {
            throw new CustomException("Invalid amount");
        }

        wallet.setBalance(wallet.getBalance() + amount);

        return walletRepository.save(wallet);
    }

    @Transactional
    public String transferMoney(Long receiverId,Double amount,String email) {

        if (amount <= 0) {
            throw new CustomException("Invalid amount");
        }
        User sender = userService.getUserByEmail(email);

        Wallet senderWallet = walletRepository.findByUserIdForUpdate(sender.getId());
        Wallet receiverWallet = walletRepository.findByUserIdForUpdate(receiverId);
        if (sender.getId().equals(receiverId)) {
            throw new CustomException("Sender and receiver cannot be the same");
        }
        
        if (sender == null) {
            throw new CustomException("Sender not found");
        }
        if (senderWallet == null || receiverWallet == null) {
            throw new CustomException("Wallet not found");
        }

        if (senderWallet.getBalance() < amount) {
            throw new CustomException("Insufficient balance");
        }

        senderWallet.setBalance(senderWallet.getBalance() - amount);
        receiverWallet.setBalance(receiverWallet.getBalance() + amount);

        walletRepository.save(senderWallet);
        walletRepository.save(receiverWallet);

        return "Transfer successful";
    }

}