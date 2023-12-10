package ru.alex0d.investmentanalyst.dto

data class PortfolioInfoDto(
    var totalValue: Double,
    var totalProfit: Double,
    var totalProfitPercent: Double,
    var stocks: List<PortfolioStockInfoDto>
)

data class PortfolioStockInfoDto(
    var ticker: String,
    var name: String,
    var amount: Int,
    var price: Double,
    var totalValue: Double,
    var profit: Double,
    var profitPercent: Double
)
