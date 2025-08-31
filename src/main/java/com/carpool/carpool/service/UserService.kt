package com.carpool.carpool.service

import com.carpool.carpool.dao.UserDao
import com.carpool.carpool.dto.AuthResponse
import com.carpool.carpool.dto.User
import com.carpool.carpool.entity.login.LoginUserRequest
import com.carpool.carpool.entity.refreshToken.RefreshTokenRequest
import com.carpool.carpool.entity.refreshToken.RefreshTokenResponse
import com.carpool.carpool.entity.register.RegisterUserRequest
import com.carpool.carpool.util.EncryptionDecryptionAES.hashPassword
import com.carpool.carpool.util.EncryptionDecryptionAES.matches
import com.carpool.carpool.util.JwtUtils
import com.carpool.carpool.util.ResponseStructure
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.util.*

@Service
open class UserService {
    @Autowired
    private lateinit var userDao: UserDao

    @Autowired
    private lateinit var jwtUtil: JwtUtils

    @Value("\${security.jwt.expiration-time}")
    private val ACCESS_EXPIRATION: Long = 0

    @Value("\${security.jwt.refresh-time}")
    private val REFRESH_EXPIRATION: Long = 0


    companion object {
        private const val CONFIRM_ACCOUNT = "confirm-account"
        private val RESET_PASSWORD = "reset-password"
    }

    fun signup(request: RegisterUserRequest): ResponseStructure<User> {
        val responseStructure = ResponseStructure<User>()
        val password = request.password?.hashPassword()
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
        } catch (_: Exception) {
            responseStructure.data = null
            responseStructure.statusCode = HttpStatus.FOUND.value()
            responseStructure.message = "User Found"
        }
        return responseStructure
    }

    fun login(userRequest: LoginUserRequest): ResponseStructure<AuthResponse?> {
        val responseStructure = ResponseStructure<AuthResponse?>()
        val user = userDao.getUserByEmail(userRequest.email)?.orElse(null)
        val isTokenMatch = matches(userRequest.password, user?.password.toString());
        if (user == null || !isTokenMatch) {
            responseStructure.data = null
            responseStructure.statusCode = HttpStatus.UNAUTHORIZED.value()
            responseStructure.message = if (isTokenMatch) "Invalid credentials" else "User has failed to login"
            return responseStructure
        }
        val accessToken = jwtUtil.generateAccessToken(user.email)
        val refreshToken = jwtUtil.generateRefreshToken(user.email)
        userDao.saveToken(user.email, accessToken, refreshToken)
        responseStructure.data = AuthResponse(accessToken, refreshToken)
        responseStructure.statusCode = HttpStatus.OK.value()
        responseStructure.message = "User logged in successfully"

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
        if (user?.isEmpty == true) {
            responseStructure.data = null
            responseStructure.statusCode = HttpStatus.NOT_FOUND.value()
            responseStructure.message = "User not found"
        }
        if (user?.isPresent == true) {
            val userData = user.get()
            userData.token = UUID.randomUUID().toString()
            userData.setUpdatedAt()
            userDao.sendMail(userData.email, "Reset Password!", RESET_PASSWORD, userData.token);
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
            val encryptedPassword = password?.hashPassword()
            user.password = encryptedPassword
            user.token = null
            userDao.saveUser(user)
            responseStructure.data = user
            responseStructure.statusCode = HttpStatus.OK.value()
            responseStructure.message = "Password reset successful, please login again"
        } else {
            responseStructure.data = null
            responseStructure.statusCode = HttpStatus.NOT_FOUND.value()
            responseStructure.message = "User not found"
        }
        return responseStructure
    }

    fun refreshToken(request: RefreshTokenRequest): ResponseStructure<RefreshTokenResponse> {
        val token = request.refreshToken
        val userName = jwtUtil.extractUserName(token)
        val tokenRequest = userDao.getRefreshTokenByUserName(userName)
        val isValidToken = (token == tokenRequest.refreshToken)
                && !jwtUtil.isTokenExpired(token)
                && !jwtUtil.isTokenExpired(tokenRequest.refreshToken)
        val responseStructure = ResponseStructure<RefreshTokenResponse>()
        return if (isValidToken) {
            val userName = jwtUtil.extractUserName(token)
            val newAccessToken = jwtUtil.generateAccessToken(userName)
            val newRefreshToken = jwtUtil.generateRefreshToken(userName)
            userDao.saveToken(userName, newAccessToken, newRefreshToken)
            responseStructure.statusCode = 200
            responseStructure.message = "Generated new access token"
            responseStructure.data = RefreshTokenResponse(newAccessToken, newRefreshToken)
            responseStructure
        } else {
            responseStructure.message = "Invalid or expired refresh token"
            responseStructure.data = null
            responseStructure.statusCode = 403
            responseStructure
        }
    }
}