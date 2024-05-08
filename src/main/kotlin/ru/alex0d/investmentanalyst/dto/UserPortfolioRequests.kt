package ru.alex0d.investmentanalyst.dto

data class BuyStockRequest(
    var uid: String,
    var amount: Int
)

data class SellStockRequest(
    var uid: String,
    var amount: Int
)