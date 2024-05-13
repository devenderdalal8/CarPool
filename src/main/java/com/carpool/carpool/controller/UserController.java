package com.carpool.carpool.controller;

import com.carpool.carpool.dto.User;
import com.carpool.carpool.service.UserService;
import com.carpool.carpool.entity.login.LoginUserRequest;
import com.carpool.carpool.entity.register.RegisterUserRequest;
import com.carpool.carpool.util.ResponseStructure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    // save student
    @PostMapping("/signup")
    public ResponseStructure<User> saveUser(@RequestBody RegisterUserRequest userRequest) {
        return userService.signup(userRequest);
    }

    @PostMapping("/login")
    public ResponseStructure<User> login(@RequestBody LoginUserRequest userRequest) {
        return userService.login(userRequest);
    }

    @RequestMapping(value = "/confirm-account", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseStructure<User> confirmUserAccount(@RequestParam("token") String confirmationToken) {
        return userService.confirmAccount(confirmationToken);
    }

    @PostMapping("/forgot-password")
    public ResponseStructure<User> forgotPass(@RequestParam("email") String email) {
        return userService.forgotPass(email);
    }

    @RequestMapping(value = "/reset-password", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseStructure<User> resetPass(@RequestParam(value ="token") String token , @RequestParam(value ="password") String password) {
        return userService.resetPassword(token,password);
    }

}