package com.carpool.carpool.util

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

object EncryptionDecryptionAES {
    private lateinit var cipher: Cipher
    private const val KEY_LENGTH = 128
    private lateinit var secretKey: SecretKey
    val encoder = BCryptPasswordEncoder()

    fun String.hashPassword(): String = encoder.encode(this)

    fun matches(rawPassword: String, hashed: String): Boolean {
        return encoder.matches(rawPassword, hashed)
    }

}
