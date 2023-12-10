package ru.alex0d.investmentanalyst.dto

data class UpdateUserDto(
    var firstname: String?,
    var lastname: String?,
    var email: String?,
    var role: String?
)