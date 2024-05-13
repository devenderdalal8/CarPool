package com.carpool.carpool.entity.register;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public class RegisterUserRequest {
    @NotBlank(message = "full name should not be blank")
    private String fullName;
    @NotBlank(message = "email should not be blank")
    private String email;
    @NotBlank(message = "password should not be blank")
    private String password;

    public RegisterUserRequest(String fullName, String email, String password) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}