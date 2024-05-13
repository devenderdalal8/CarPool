package com.carpool.carpool.dao;

import com.carpool.carpool.dto.User;
import com.carpool.carpool.repository.UserRepository;
import com.carpool.carpool.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import javax.naming.Context;
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

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Async
    public void sendMail(String email , String subject, String endpoint, String token) {
        emailService.sendEmailWithoutHtml(email, subject, endpoint, token);
    }

    public Optional<User> getToken(String token) {
        return userRepository.findByToken(token);
    }
}
