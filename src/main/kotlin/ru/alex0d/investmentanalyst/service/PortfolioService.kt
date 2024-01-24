package ru.alex0d.investmentanalyst.service

import kotlinx.serialization.json.Json
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import ru.alex0d.investmentanalyst.api.finage.MarketNews
import ru.alex0d.investmentanalyst.api.fmpcloud.Quote
import ru.alex0d.investmentanalyst.api.makeRequest
import ru.alex0d.investmentanalyst.dto.BuyStockRequest
import ru.alex0d.investmentanalyst.dto.PortfolioInfoDto
import ru.alex0d.investmentanalyst.dto.PortfolioStockInfoDto
import ru.alex0d.investmentanalyst.dto.SellStockRequest
import ru.alex0d.investmentanalyst.model.PortfolioStock
import ru.alex0d.investmentanalyst.model.User
import ru.alex0d.investmentanalyst.repository.PortfolioRepository

@Service
class PortfolioService(
    private val portfolioRepository: PortfolioRepository
) {
    @Value("\${application.fmpcloud.key}")
    private lateinit var fmpCloudApiKey: String

    @Value("\${application.finage.key}")
    private lateinit var finageApiKey: String

    fun getPortfolio(): PortfolioInfoDto {
        val user = SecurityContextHolder.getContext().authentication.principal as User
        val stocks = portfolioRepository.getPortfolioByUser(user).stocks

        val stockDtos = stocks.map { stock ->
            val body = makeRequest("https://fmpcloud.io/api/v3/quote/${stock.ticker}?apikey=$fmpCloudApiKey")
                ?: throw Exception("Empty response body")
            val requestedStock = Json.decodeFromString<List<Quote>>(body)[0]

            val totalValue = requestedStock.price * stock.amount
            val profit = totalValue - stock.amount * stock.buyingPrice
            val profitPercent = profit / (stock.amount * stock.buyingPrice) * 100

            PortfolioStockInfoDto(
                requestedStock, stock.amount, totalValue, profit, profitPercent
            )
        }

        val totalValue = stockDtos.sumOf { it.totalValue }
        val totalProfit = stockDtos.sumOf { it.profit }
        val totalProfitPercent = totalProfit / totalValue * 100

        return PortfolioInfoDto(
            totalValue, totalProfit, totalProfitPercent, stockDtos
        )
    }

    fun getPortfolioNews(): List<MarketNews> {
        val user = SecurityContextHolder.getContext().authentication.principal as User
        val stocks = portfolioRepository.getPortfolioByUser(user).stocks

        val news = stocks.map { stock ->
            val body = makeRequest("https://api.finage.co.uk/news/market/${stock.ticker}?apikey=$finageApiKey")
                ?: return emptyList()
            Json.decodeFromString<MarketNews>(body)
        }
        return news
    }

    fun buyStock(buyStockRequest: BuyStockRequest): Boolean {
        val body = makeRequest("https://fmpcloud.io/api/v3/quote/${buyStockRequest.ticker}?apikey=$fmpCloudApiKey")
            ?: return false
        val requestedStock = Json.decodeFromString<List<Quote>>(body)[0]

        val user = SecurityContextHolder.getContext().authentication.principal as User
        val portfolio = portfolioRepository.getPortfolioByUser(user)

        var stock = portfolio.stocks.find { it.ticker == buyStockRequest.ticker }

        stock?.apply {
            buyingPrice = (buyingPrice * amount + buyStockRequest.amount * requestedStock.price) / (amount + buyStockRequest.amount)
            amount += buyStockRequest.amount
        } ?: run {
            stock = PortfolioStock(portfolio, buyStockRequest.ticker, buyStockRequest.amount, requestedStock.price)
            portfolio.stocks.add(stock!!)
        }

        portfolioRepository.save(portfolio)
        return true
    }

    fun sellStock(sellStockRequest: SellStockRequest): Boolean {
        val user = SecurityContextHolder.getContext().authentication.principal as User
        val portfolio = portfolioRepository.getPortfolioByUser(user)

        val stock = portfolio.stocks.find { it.ticker == sellStockRequest.ticker } ?: return false
        if (stock.amount < sellStockRequest.amount) return false
        if (stock.amount == sellStockRequest.amount) {
            stock.portfolio = null
            portfolio.stocks.remove(stock)
        } else {
            val body = makeRequest("https://fmpcloud.io/api/v3/quote/${sellStockRequest.ticker}?apikey=$fmpCloudApiKey")
                ?: return false
            val requestedStock = Json.decodeFromString<List<Quote>>(body)[0]

            stock.buyingPrice =
                (stock.buyingPrice * stock.amount - sellStockRequest.amount * requestedStock.price!!) / (stock.amount - sellStockRequest.amount)
            stock.amount -= sellStockRequest.amount
        }
        portfolioRepository.save(portfolio)
        return true
    }
}