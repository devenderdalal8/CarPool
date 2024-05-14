package com.carpool.carpool.entity.register;

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

    public String getFullName() {
        return fullName;
    }

    @Override
    public String toString() {
        return "RegisterUserRequest{" +
                "fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}