package com.wallet.service;

import com.wallet.model.Wallet;
import com.wallet.model.User;
import com.wallet.repository.WalletRepository;
import com.wallet.service.UserService;
import com.wallet.model.Transaction;
import com.wallet.repository.TransactionRepository;
import com.wallet.exception.CustomException;
import com.wallet.dto.TransactionResponseDTO;
import com.wallet.dto.TransferResponseDTO;
import com.wallet.dto.UserWalletDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class WalletService {
    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CacheManager cacheManager;

    @Cacheable(value = "wallet", key = "#email")
    public Wallet getWallet(Long userId,String email) {

        System.out.println("Fetching from DB...");
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

        walletRepository.save(wallet);
        Transaction txn = new Transaction();
        txn.setSenderId(null); // system
        txn.setReceiverId(user.getId());
        txn.setAmount(amount);
        txn.setStatus("SUCCESS");
        txn.setType("CREDITLOAD");
        txn.setCreatedAt(LocalDateTime.now());

        transactionRepository.save(txn);

        return wallet;
    }

    @Transactional
    public TransferResponseDTO transferMoney(Long receiverId,Double amount,String email) {

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
        
        // Get receiver email for cache eviction
        User receiver = userService.getUserById(receiverId);

        if (senderWallet.getBalance() < amount) {
            throw new CustomException("Insufficient balance");
        }

        senderWallet.setBalance(senderWallet.getBalance() - amount);
        receiverWallet.setBalance(receiverWallet.getBalance() + amount);

        walletRepository.save(senderWallet);
        walletRepository.save(receiverWallet);

        UserWalletDTO senderDTO =
            new UserWalletDTO(sender.getId(), senderWallet.getBalance());

        UserWalletDTO receiverDTO =
            new UserWalletDTO(receiverId, receiverWallet.getBalance());

        Transaction txn = new Transaction();
        txn.setSenderId(sender.getId());
        txn.setReceiverId(receiverId);
        txn.setAmount(amount);
        txn.setStatus("SUCCESS");
        txn.setType("TRANSFER");
        txn.setCreatedAt(LocalDateTime.now());

        transactionRepository.save(txn);

        // Invalidate cache for both sender and receiver
        cacheManager.getCache("wallet").evict(email);
        cacheManager.getCache("wallet").evict(receiver.getEmail());

        return new TransferResponseDTO(
            senderDTO,
            receiverDTO
        );
    }

    public Page<TransactionResponseDTO> getTransactions(String email, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        User user = userService.getUserByEmail(email);
        Page<Transaction> transactions= transactionRepository
            .findBySenderIdOrReceiverId(user.getId(), user.getId(), pageable);
        return transactions.map(txn -> {

            TransactionResponseDTO dto = new TransactionResponseDTO();

            dto.setAmount(txn.getAmount());
            dto.setType(txn.getType());
            dto.setStatus(txn.getStatus());
            dto.setCreatedAt(txn.getCreatedAt().toString());

            dto.setSenderId(txn.getSenderId());
            dto.setReceiverId(txn.getReceiverId());

            // Core logic
            if ("CREDITLOAD".equals(txn.getType())) {

                dto.setType("CREDITLOAD");

            } else if ("TRANSFER".equals(txn.getType())) {

                if (txn.getSenderId().equals(user.getId())) {
                    dto.setType("DEBIT");   // user sent money
                } else {
                    dto.setType("CREDIT");  // user received money
                }
            }

            return dto;
        });
    }

}