package com.wallet.service;

import com.wallet.model.User;
import com.wallet.model.Wallet;
import com.wallet.model.RefreshToken;
import com.wallet.repository.UserRepository;
import com.wallet.repository.WalletRepository;
import com.wallet.repository.RefreshTokenRepository;
import com.wallet.dto.UserResponseDTO;
import com.wallet.dto.AuthResponseDTO;
import com.wallet.exception.CustomException;
import com.wallet.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private JwtUtil jwtUtil;

    // public String login(String email, String password) {
        
    //     User user = userRepository.findByEmail(email);

    //     if (user == null || !user.getPassword().equals(password)) {
    //         throw new CustomException("Invalid credentials");
    //     }
    //     return jwtUtil.generateToken(email);
    // }

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    public AuthResponseDTO login(String email, String password) {

        User user = userRepository.findByEmail(email);

        if (user == null || !user.getPassword().equals(password)) {
            throw new CustomException("Invalid credentials");
        }

        String accessToken = jwtUtil.generateToken(email);
        String refreshToken = jwtUtil.generateRefreshToken(email);

        RefreshToken existingToken =
            refreshTokenRepository.findByUser_Id(user.getId());

        if (existingToken != null) {
            //  Update existing token
            existingToken.setToken(refreshToken);
            existingToken.setExpiryDate(
                    Instant.now().plusSeconds(7 * 24 * 60 * 60)
            );

            refreshTokenRepository.save(existingToken);
        } else {
            //  Create new token
            RefreshToken newToken = new RefreshToken();
            newToken.setUser(user);
            newToken.setToken(refreshToken);
            newToken.setExpiryDate(
                    Instant.now().plusSeconds(7 * 24 * 60 * 60)
            );

            refreshTokenRepository.save(newToken);
        }

        return new AuthResponseDTO(accessToken, refreshToken);
    }

    public AuthResponseDTO refreshAccessToken(String token) {

        RefreshToken refreshToken = refreshTokenRepository.findByToken(token);

        if (refreshToken == null) {
            throw new CustomException("Invalid refresh token");
        }

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            throw new CustomException("Refresh token expired");
        }

        String userEmail = refreshToken.getUser().getEmail();
        String newAccessToken = jwtUtil.generateToken(userEmail);

        return new AuthResponseDTO(newAccessToken, token);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserResponseDTO createUser(User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new CustomException("Email already exists");
        }
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