package com.carpool.carpool.dao;

import com.carpool.carpool.dto.User;
import com.carpool.carpool.repository.UserRepository;
import com.carpool.carpool.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserDao {
    @Autowired
    UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Cacheable(value = "userCache" , key = "#email")
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Async
    public void sendMail(String email, String subject, String endpoint, String token) {
        emailService.sendEmailWithoutHtml(email, subject, endpoint, token);
    }

    @Cacheable(value = "userToken" , key = "#token")
    public Optional<User> getToken(String token) {
        return userRepository.findByToken(token);
    }
}
