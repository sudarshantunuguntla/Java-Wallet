package com.wallet.service;

import com.wallet.model.User;
import com.wallet.model.Wallet;
import com.wallet.repository.UserRepository;
import com.wallet.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;

    public User createUser(User user) {
        User savedUser = userRepository.save(user);
        Wallet wallet = new Wallet();
        wallet.setUser(savedUser);
        wallet.setBalance(0.0);

        walletRepository.save(wallet);
        
        return savedUser;
    }

    @Transactional

    public Wallet addMoney(Long userId, Double amount) {
        Wallet wallet = walletRepository.findByUserIdForUpdate(userId);
        if (wallet == null) {
            throw new RuntimeException("Wallet not found");
        }

        if (amount <= 0) {
            throw new RuntimeException("Invalid amount");
        }

        wallet.setBalance(wallet.getBalance() + amount);

        return walletRepository.save(wallet);
    }
    
    @Transactional

    public String transferMoney(Long senderId,Long receiverId,Double amount){
        if(amount<=0){
            throw new RuntimeException("Invalid amount");
        }
        Wallet senderWallet=walletRepository.findByUserIdForUpdate(senderId);
        Wallet receiverWallet=walletRepository.findByUserIdForUpdate(receiverId);

        if(senderWallet==null || receiverWallet==null){
            throw new RuntimeException("Wallet not found");
        }

        if (senderWallet.getBalance()<amount){
            throw new RuntimeException("Insufficient balance");
        }

        senderWallet.setBalance(senderWallet.getBalance() - amount);
        receiverWallet.setBalance(receiverWallet.getBalance() + amount);

        walletRepository.save(senderWallet);
        walletRepository.save(receiverWallet);

        return "Transfer successful";

    }
}
