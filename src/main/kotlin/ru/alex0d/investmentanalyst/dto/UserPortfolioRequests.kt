package ru.alex0d.investmentanalyst.dto

data class BuyStockRequest(
    var ticker: String,
    var amount: Int
)

data class SellStockRequest(
    var ticker: String,
    var amount: Int
)