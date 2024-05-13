package com.carpool.carpool.util

import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

object EncryptionDecryptionAES {
    private lateinit var cipher: Cipher
    private const val KEY_LENGTH = 128
    private lateinit var secretKey: SecretKey

    init {
        initialize()
    }

    private fun initialize() {
        cipher = Cipher.getInstance("AES")
        secretKey = KEY_LENGTH.generateKey()
    }

    private fun Int.generateKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.init(this)
        val key = keyGenerator.generateKey()
        return key
    }

    @OptIn(ExperimentalEncodingApi::class)
    fun encrypt(password: String): String {
        val passwordByte = password.toByteArray()
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val encryptedByte = cipher.doFinal(passwordByte)
        val encoder = Base64.encode(encryptedByte)
        return encoder
    }

    @OptIn(ExperimentalEncodingApi::class)
    fun decrypt(password: String): String {
        val decoder = Base64.decode(password)
        cipher.init(Cipher.DECRYPT_MODE, secretKey)
        val decryptedByte = cipher.doFinal(decoder)
        val decryptedText = String(decryptedByte)
        return decryptedText
    }
}
