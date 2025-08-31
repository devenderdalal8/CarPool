package com.carpool.carpool.entity.register

import jakarta.validation.constraints.NotBlank
import lombok.AllArgsConstructor
import lombok.Getter
import lombok.Setter
import lombok.ToString

@ToString
@Getter
@Setter
@AllArgsConstructor
data class RegisterUserRequest(
    val fullName: @NotBlank(message = "full name should not be blank") String? = null,
    val email: @NotBlank(message = "email should not be blank") String? = null,
    val password: @NotBlank(message = "password should not be blank") String? = null,
)