package com.wallet.service;

import com.wallet.model.User;
import com.wallet.model.Wallet;
import com.wallet.repository.UserRepository;
import com.wallet.repository.WalletRepository;
import com.wallet.dto.UserResponseDTO;
import com.wallet.exception.CustomException;
import com.wallet.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public String login(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user == null || !user.getPassword().equals(password)) {
            throw new CustomException("Invalid credentials");
        }
        return jwtUtil.generateToken(email);
    }

    public UserResponseDTO createUser(User user) {
        User savedUser = userRepository.save(user);
        Wallet wallet = new Wallet();
        wallet.setUser(savedUser);
        wallet.setBalance(0.0);

        walletRepository.save(wallet);
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(savedUser.getId());
        dto.setEmail(savedUser.getEmail());
        return dto;
    }
}