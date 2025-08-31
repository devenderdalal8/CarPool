package com.carpool.carpool.repository

import com.carpool.carpool.dto.refreshToken.RefreshToken
import jakarta.transaction.Transactional
import org.apache.kafka.common.protocol.types.Field
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface RefreshTokenRepository : JpaRepository<RefreshToken, Long?> {
    fun findByUserEmail(userEmail : String) : RefreshToken
    @Transactional
    fun deleteByUserEmail(userEmail: String): Long
}
