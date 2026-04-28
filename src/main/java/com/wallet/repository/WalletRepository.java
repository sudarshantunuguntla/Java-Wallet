package com.wallet.repository;

import com.wallet.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import jakarta.persistence.LockModeType;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Wallet findByUserId(Long userId);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT w FROM Wallet w WHERE w.user.id = :userId")
    Wallet findByUserIdForUpdate(Long userId);
}