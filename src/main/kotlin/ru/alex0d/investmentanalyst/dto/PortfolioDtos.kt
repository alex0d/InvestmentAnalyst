package ru.alex0d.investmentanalyst.dto

import ru.alex0d.investmentanalyst.api.fmpcloud.Quote

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
) {
    constructor(
        requestedQuote: Quote,
        amount: Int,
        totalValue: Double,
        profit: Double,
        profitPercent: Double
    ) : this(
        requestedQuote.symbol,
        requestedQuote.name,
        amount,
        requestedQuote.price,
        totalValue,
        profit,
        profitPercent
    )
}
