package com.wallet.repository;

import com.wallet.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Page<Transaction> findBySenderId(Long senderId, Pageable pageable);

    Page<Transaction> findByReceiverId(Long receiverId, Pageable pageable);

    Page<Transaction> findBySenderIdOrReceiverId(
        Long senderId,
        Long receiverId,
        Pageable pageable);
}