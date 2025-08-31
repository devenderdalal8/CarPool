package com.carpool.carpool.dao;

import com.carpool.carpool.dto.User;
import com.carpool.carpool.dto.refreshToken.RefreshToken;
import com.carpool.carpool.repository.RefreshTokenRepository;
import com.carpool.carpool.repository.UserRepository;
import com.carpool.carpool.service.EmailService;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserDao {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private EmailService emailService;

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Async
    public void sendMail(String email, String subject, String endpoint, String token) {
        emailService.sendEmailWithoutHtml(email, subject, endpoint, token);
    }

    public Optional<User> getToken(String token) {
        return userRepository.findByToken(token);
    }

    public RefreshToken getRefreshTokenByUserName(String email) {
        return refreshTokenRepository.findByUserEmail(email);
    }

    @Async
    public void saveToken(String email, String accessToken, String refreshToken) {
        RefreshToken token = new RefreshToken();
        token.setRefreshToken(refreshToken);
        token.setAccessToken(accessToken);
        token.setUserEmail(email);
        refreshTokenRepository.deleteByUserEmail(email);
        refreshTokenRepository.save(token);
    }
}
