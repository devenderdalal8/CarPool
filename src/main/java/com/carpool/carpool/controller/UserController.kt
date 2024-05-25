package com.carpool.carpool.controller

import com.carpool.carpool.dto.User
import com.carpool.carpool.entity.login.LoginUserRequest
import com.carpool.carpool.entity.register.RegisterUserRequest
import com.carpool.carpool.service.UserService
import com.carpool.carpool.util.ResponseStructure
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
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
class UserController {
    @Autowired
    lateinit var userService: UserService
    var logger = LoggerFactory.getLogger(UserController::class.java)

    /**
     * This method is used to save a new user in the system. It takes a RegisterUserRequest object as input, which contains the user's registration details.
     * The method then calls the corresponding method in the UserService to save the user in the database.
     *
     * @param userRequest The RegisterUserRequest object containing the user's registration details.
     * @return A ResponseStructure object containing the saved User object.
     */
    @PostMapping("/signup")
    fun saveUser(@RequestBody userRequest: RegisterUserRequest): ResponseStructure<User> {
        return userService.signup(userRequest)
    }

    /**
     * This method is used to authenticate a user and return their details. It takes a LoginUserRequest object as input, which contains the user's login credentials.
     * The method then calls the corresponding method in the UserService to authenticate the user and return their details.
     *
     * @param userRequest The LoginUserRequest object containing the user's login credentials.
     * @return A ResponseStructure object containing the authenticated User object.
     */
    @PostMapping("/login")
    fun login(@RequestBody userRequest: LoginUserRequest): ResponseStructure<User?> {
        return userService.login(userRequest)
    }

    /**
     * This method is used to confirm a user's account. It takes a confirmation token as input, which is sent to the user's email during the registration process.
     * The method then calls the corresponding method in the UserService to confirm the user's account and return their details.
     *
     * @param confirmationToken The confirmation token sent to the user's email.
     * @return A ResponseStructure object containing the confirmed User object.
     */
    @RequestMapping(value = ["/confirm-account"], method = [RequestMethod.GET, RequestMethod.POST])
    fun confirmUserAccount(@RequestParam("token") confirmationToken: String): ResponseStructure<User?> {
        return userService.confirmAccount(confirmationToken)
    }

    /**
     * This method is used to initiate the password reset process. It takes an email address as input, which is used to send a reset token to the user's email.
     * The user can then use this token to reset their password using the `resetPass` method.
     * The method then calls the corresponding method in the UserService to send the reset token to the user's email.
     *
     * @param email The email address of the user who wants to reset their password.
     * @return A ResponseStructure object containing the User object with a reset token.
     */
    @PostMapping("/forgotPassword")
    fun forgotPass(@RequestParam("email") email: String): ResponseStructure<User?> {
        return userService.forgotPass(email)
    }

    /**
     * This method is used to reset the user's password. It takes a token and a new password as input.
     * The token is used to verify the user's identity and ensure that the password reset request is legitimate.
     * The new password is used to update the user's password in the database.
     * The method then calls the corresponding method in the UserService to reset the user's password and return their updated details.
     *
     * @param token    The token used to verify the user's identity.
     * @param password The new password for the user.
     * @return A ResponseStructure object containing the updated User object.
     */
    @RequestMapping(value = ["/resetPassword"], method = [RequestMethod.GET, RequestMethod.POST])
    fun resetPass(@RequestParam(value = "token") token: String, @RequestParam(value = "password") password: String): ResponseStructure<User?> {
        return userService.resetPassword(token, password)
    }

    @GetMapping("/test")
    fun test():String{
        return "test"
    }
}