package com.carpool.carpool.service

import com.carpool.carpool.dao.UserDao
import com.carpool.carpool.dto.User
import com.carpool.carpool.entity.login.LoginUserRequest
import com.carpool.carpool.entity.register.RegisterUserRequest
import com.carpool.carpool.util.EncryptionDecryptionAES.decrypt
import com.carpool.carpool.util.EncryptionDecryptionAES.encrypt
import com.carpool.carpool.util.ResponseStructure
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.util.*

@Service
open class UserService {
    @Autowired
    private lateinit var userDao: UserDao

    companion object {
        private const val CONFIRM_ACCOUNT = "confirm-account"
        private val RESET_PASSWORD = "reset-password"
    }

    fun signup(request: RegisterUserRequest): ResponseStructure<User> {
        val responseStructure = ResponseStructure<User>()
        val password = request.password.encrypt()
        val user = User(request.fullName, request.email, password)
        try {
            val response = userDao.saveUser(user)
            userDao.sendMail(user.email, "Account Confirmation!", CONFIRM_ACCOUNT, user.token)
            responseStructure.apply {
                if (response != null) {
                    data = response
                    statusCode = HttpStatus.CREATED.value()
                    message = "Verify email by the link sent on your email address"
                } else {
                    data = null
                    statusCode = HttpStatus.CREATED.value()
                    message = "User has failed to save"
                }
            }
        } catch (ex: Exception) {
            responseStructure.data = null
            responseStructure.statusCode = HttpStatus.FOUND.value()
            responseStructure.message = "User Found"
        }
        return responseStructure
    }

    fun login(userRequest: LoginUserRequest): ResponseStructure<User?> {
        val responseStructure = ResponseStructure<User?>()
        val user = userDao.getUserByEmail(userRequest.email).orElse(null)
        if (user != null) {
            val password = decrypt(user.password)
            if (password == userRequest.password) {
                responseStructure.data = user
                responseStructure.statusCode = HttpStatus.OK.value()
                responseStructure.message = "User logged in successfully"
            }
        } else {
            responseStructure.data = null
            responseStructure.statusCode = HttpStatus.UNAUTHORIZED.value()
            responseStructure.message = "User has failed to login"
        }
        return responseStructure
    }

    fun confirmAccount(token: String?): ResponseStructure<User?> {
        val responseStructure = ResponseStructure<User?>()
        val userToken = userDao.getToken(token)
        if (userToken.isEmpty) {
            responseStructure.data = null
            responseStructure.statusCode = HttpStatus.NOT_FOUND.value()
            responseStructure.message = "User not found"
        }
        if (userToken.isPresent) {
            val user = userToken.get()
            user.token = UUID.randomUUID().toString()
            user.enabled = true
            userDao.saveUser(user)
            responseStructure.data = user
            responseStructure.statusCode = HttpStatus.CREATED.value()
            responseStructure.message = "User has successfully confirm account"
        }

        return responseStructure
    }

    fun forgotPass(email: String?): ResponseStructure<User?> {
        val responseStructure = ResponseStructure<User?>()
        val user = userDao.getUserByEmail(email)
        if (user.isEmpty) {
            responseStructure.data = null
            responseStructure.statusCode = HttpStatus.NOT_FOUND.value()
            responseStructure.message = "User not found"
        }
        if (user.isPresent) {
            val userData = user.get()
            userData.token = UUID.randomUUID().toString()
            userData.setUpdatedAt()
            //            userDao.sendMail(userData.getEmail(), "Reset Password!", RESET_PASSWORD, userData.getToken());
            userDao.saveUser(userData)
            responseStructure.data = userData
            responseStructure.statusCode = HttpStatus.OK.value()
            responseStructure.message = "Verify email by the link sent on your email address"
        }
        return responseStructure
    }

    fun resetPassword(token: String?, password: String?): ResponseStructure<User?> {
        val responseStructure = ResponseStructure<User?>()
        val userOptional = userDao.getToken(token)
        if (userOptional.isPresent) {
            val user = userOptional.get()
            val encryptedPassword = password?.encrypt()
            user.password = encryptedPassword
            user.token = token
            userDao.saveUser(user)
            responseStructure.data = user
            responseStructure.statusCode = HttpStatus.OK.value()
            responseStructure.message = "Verify email by the link sent on your email address"
        } else {
            responseStructure.data = null
            responseStructure.statusCode = HttpStatus.NOT_FOUND.value()
            responseStructure.message = "User not found"
        }
        return responseStructure
    }
}