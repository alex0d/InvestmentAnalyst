package ru.alex0d.investmentanalyst.dto

import java.math.BigDecimal

data class CandlesRequestDto(
    val uid: String,
    val from: Long,
    val to: Long,
    val interval: String
)

data class CandlesResponseDto(
    val timestamp: Long,
    val open: BigDecimal,
    val close: BigDecimal,
    val high: BigDecimal,
    val low: BigDecimal,
    val volume: Long
)