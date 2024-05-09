package ru.alex0d.investmentanalyst.dto

data class RegisterRequest(
    val firstname: String,
    val lastname: String? = null,
    val email: String,
    val password: String
)

data class AuthenticationRequest(
    val email: String,
    val password: String
)

data class AuthenticationResponse(
    val accessToken: String,
    val refreshToken: String
)

data class RefreshTokenRequest(
    val refreshToken: String
)