package com.wallet.repository;

import com.wallet.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    RefreshToken findByToken(String token);
    
    RefreshToken findByUser_Id(Long userId);
    void deleteByUserId(Long userId);
}