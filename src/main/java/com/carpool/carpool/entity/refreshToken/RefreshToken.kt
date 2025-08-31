package com.carpool.carpool.entity.refreshToken

import lombok.AllArgsConstructor

@AllArgsConstructor
data class RefreshTokenRequest(val refreshToken: String = "")

data class RefreshTokenResponse(
    val accessToken: String = "", val refreshToken: String = ""
)