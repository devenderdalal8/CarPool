package com.carpool.carpool.controller

import com.carpool.carpool.dto.AuthResponse
import com.carpool.carpool.dto.User
import com.carpool.carpool.entity.login.LoginUserRequest
import com.carpool.carpool.entity.refreshToken.RefreshTokenRequest
import com.carpool.carpool.entity.refreshToken.RefreshTokenResponse
import com.carpool.carpool.entity.register.RegisterUserRequest
import com.carpool.carpool.service.UserService
import com.carpool.carpool.util.ResponseStructure
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * This class handles user-related operations such as registration, login, password reset, and account confirmation.
 */
@RestController
@RequestMapping("/api/user")
class UserController {
    @Autowired
    lateinit var userService: UserService
    var logger = LoggerFactory.getLogger(UserController::class.java)

    @GetMapping("/")
    fun indexOf(): String {
        return "Api is Working"
    }

    @PostMapping("/signup")
    fun signup(@RequestBody userRequest: RegisterUserRequest): ResponseStructure<User> {
        logger.info("Signup API hit with email: ${userRequest.email}")
        return userService.signup(userRequest)
    }

    @PostMapping("/login")
    fun login(@RequestBody userRequest: LoginUserRequest): ResponseStructure<AuthResponse?> {
        return userService.login(userRequest)
    }

    @RequestMapping(value = ["/confirm-account"], method = [RequestMethod.GET, RequestMethod.POST])
    fun confirmUserAccount(@RequestParam("token") confirmationToken: String): ResponseStructure<User?> {
        return userService.confirmAccount(confirmationToken)
    }

    @PostMapping("/forgotPassword")
    fun forgotPass(@RequestParam("email") email: String): ResponseStructure<User?> {
        return userService.forgotPass(email)
    }

    @RequestMapping(value = ["/resetPassword"], method = [RequestMethod.GET, RequestMethod.POST])
    fun resetPass(
        @RequestParam(value = "token") token: String,
        @RequestParam(value = "password") password: String
    ): ResponseStructure<User?> {
        return userService.resetPassword(token, password)
    }

    @PostMapping("/refresh-token")
    fun refreshToken(@RequestBody request: RefreshTokenRequest): ResponseEntity<ResponseStructure<RefreshTokenResponse>> {
        val response = userService.refreshToken(request)
        logger.info("api called ${response.data} => ${response.statusCode}")
        return ResponseEntity.status(response.statusCode).body(response)
    }
}