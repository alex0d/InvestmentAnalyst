package ru.alex0d.investmentanalyst.dto

import java.math.BigDecimal

data class PortfolioInfoDto(
    var totalValue: BigDecimal,
    var totalProfit: BigDecimal,
    var totalProfitPercent: BigDecimal,
    var stocks: List<PortfolioStockInfoDto>
)

data class PortfolioStockInfoDto(
    var uid: String,
    var ticker: String,
    var name: String,

    var amount: Int,
    var price: BigDecimal,

    var totalValue: BigDecimal,
    var profit: BigDecimal,
    var profitPercent: BigDecimal,

    var logoUrl: String,
    var backgroundColor: String,
    var textColor: String
)