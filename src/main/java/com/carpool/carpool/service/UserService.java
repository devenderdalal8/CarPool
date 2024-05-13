package com.carpool.carpool.service;

import com.carpool.carpool.dao.UserDao;
import com.carpool.carpool.dto.User;
import com.carpool.carpool.entity.login.LoginUserRequest;
import com.carpool.carpool.entity.register.RegisterUserRequest;
import com.carpool.carpool.util.ResponseStructure;
import com.carpool.carpool.util.EncryptionDecryptionAES;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    private final String CONFIRM_ACCOUNT = "confirm-account";
    private final String RESET_PASSWORD = "reset-password";

    public ResponseStructure<User> signup(RegisterUserRequest request) {
        ResponseStructure<User> responseStructure = new ResponseStructure<>();
        String password = EncryptionDecryptionAES.INSTANCE.encrypt(request.getPassword());
        User user = new User(request.getFullName(), request.getEmail(), password);
        User response = userDao.saveUser(user);
        userDao.sendMail(user.getEmail(), "Account Confirmation!", CONFIRM_ACCOUNT, user.getToken());
        if (response != null) {
            responseStructure.setData(response);
            responseStructure.setStatusCode(HttpStatus.CREATED.value());
            responseStructure.setMessage("Verify email by the link sent on your email address");
        } else {
            responseStructure.setData(null);
            responseStructure.setStatusCode(HttpStatus.CREATED.value());
            responseStructure.setMessage("User has failed to save");
        }
        return responseStructure;
    }

    public ResponseStructure<User> login(LoginUserRequest userRequest) {
        ResponseStructure<User> responseStructure = new ResponseStructure<>();
        User user = userDao.getUserByEmail(userRequest.getEmail()).orElse(null);
        if (user != null) {
            String password = EncryptionDecryptionAES.INSTANCE.decrypt(user.getPassword());
            if (password.equals(userRequest.getPassword())) {
                responseStructure.setData(user);
                responseStructure.setStatusCode(HttpStatus.OK.value());
                responseStructure.setMessage("User logged in successfully");
            }
        } else {
            responseStructure.setData(null);
            responseStructure.setStatusCode(HttpStatus.UNAUTHORIZED.value());
            responseStructure.setMessage("User has failed to login");
        }
        return responseStructure;
    }

    public ResponseStructure<User> confirmAccount(String token) {
        ResponseStructure<User> responseStructure = new ResponseStructure<>();
        Optional<User> userToken = userDao.getToken(token);
        if (userToken.isEmpty()) {
            responseStructure.setData(null);
            responseStructure.setStatusCode(HttpStatus.NOT_FOUND.value());
            responseStructure.setMessage("User not found");
        }
        if (userToken.isPresent()) {
            User user = userToken.get();
            user.setToken(UUID.randomUUID().toString());
            user.setEnabled(true);
            userDao.saveUser(user);
            responseStructure.setData(user);
            responseStructure.setStatusCode(HttpStatus.CREATED.value());
            responseStructure.setMessage("User has successfully confirm account");
        }

        return responseStructure;
    }

    public ResponseStructure<User> forgotPass(String email) {
        ResponseStructure<User> responseStructure = new ResponseStructure<>();
        Optional<User> user = userDao.getUserByEmail(email);
        if (user.isEmpty()) {
            responseStructure.setData(null);
            responseStructure.setStatusCode(HttpStatus.NOT_FOUND.value());
            responseStructure.setMessage("User not found");
        }
        if (user.isPresent()) {
            User userData = user.get();
            userData.setToken(UUID.randomUUID().toString());
            userData.setUpdatedAt();
//            userDao.sendMail(userData.getEmail(), "Reset Password!", RESET_PASSWORD, userData.getToken());
            userDao.saveUser(userData);
            responseStructure.setData(userData);
            responseStructure.setStatusCode(HttpStatus.OK.value());
            responseStructure.setMessage("Verify email by the link sent on your email address");
        }
        return responseStructure;
    }

    public ResponseStructure<User> resetPassword(String token, String password) {
        ResponseStructure<User> responseStructure = new ResponseStructure<>();
        Optional<User> userOptional = userDao.getToken(token);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String encryptedPassword = EncryptionDecryptionAES.INSTANCE.encrypt(password);
            user.setPassword(encryptedPassword);
            user.setToken(token);
            userDao.saveUser(user);
            responseStructure.setData(user);
            responseStructure.setStatusCode(HttpStatus.OK.value());
            responseStructure.setMessage("Verify email by the link sent on your email address");
        } else {
            responseStructure.setData(null);
            responseStructure.setStatusCode(HttpStatus.NOT_FOUND.value());
            responseStructure.setMessage("User not found");
        }
        return responseStructure;
    }
}