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
    val firstname: String,
    val lastname: String?,
    val email: String,
    val accessToken: String,
    val refreshToken: String
)

data class RefreshTokenRequest(
    val refreshToken: String
)