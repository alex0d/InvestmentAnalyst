package ru.alex0d.investmentanalyst.service

import kotlinx.serialization.json.Json
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import ru.alex0d.investmentanalyst.api.finage.MarketNews
import ru.alex0d.investmentanalyst.api.makeRequest
import ru.alex0d.investmentanalyst.api.utils.splitIntoStrings
import ru.alex0d.investmentanalyst.api.utils.toBigDecimal
import ru.alex0d.investmentanalyst.dto.BuyStockRequest
import ru.alex0d.investmentanalyst.dto.PortfolioInfoDto
import ru.alex0d.investmentanalyst.dto.PortfolioStockInfoDto
import ru.alex0d.investmentanalyst.dto.SellStockRequest
import ru.alex0d.investmentanalyst.model.PortfolioStock
import ru.alex0d.investmentanalyst.model.User
import ru.alex0d.investmentanalyst.repository.PortfolioRepository
import ru.tinkoff.piapi.contract.v1.LastPrice
import ru.tinkoff.piapi.core.InvestApi
import java.math.BigDecimal
import java.math.RoundingMode

@Service
class PortfolioService(
    private val portfolioRepository: PortfolioRepository,
    private val investApi: InvestApi
) {
    @Value("\${application.finage.key}")
    private lateinit var finageApiKey: String

    fun getPortfolio(): PortfolioInfoDto {
        val user = SecurityContextHolder.getContext().authentication.principal as User
        val stocks = portfolioRepository.getPortfolioByUser(user).stocks

        val prices = investApi.marketDataService.getLastPricesSync(stocks.map { it.uid })

        val stockDtos = stocks.map { stock ->
            val requestedPrice = prices.find { it.instrumentUid == stock.uid } ?: throw Exception("Price not found")

            val totalValue = calculateTotalValue(stock, requestedPrice)
            val profit = calculateProfit(stock, totalValue)
            val profitPercent = calculateProfitPercent(stock, profit)

            PortfolioStockInfoDto(
                uid = stock.uid,
                ticker = stock.ticker,
                name = stock.name,
                amount = stock.amount,
                averagePrice = stock.averagePrice,
                lastPrice = requestedPrice.price.toBigDecimal(),
                totalValue = totalValue,
                profit = profit,
                profitPercent = profitPercent,
                logoUrl = stock.logoUrl,
                backgroundColor = stock.backgroundColor,
                textColor = stock.textColor
            )
        }

        val totalValue = stockDtos.sumOf { it.totalValue }.setScale(2, RoundingMode.HALF_UP)
        val totalProfit = stockDtos.sumOf { it.profit }.setScale(2, RoundingMode.HALF_UP)
        val totalProfitPercent =
            (if (totalValue.compareTo(BigDecimal.ZERO) != 0) totalProfit / totalValue * BigDecimal(100) else BigDecimal.ZERO)
                .setScale(2, RoundingMode.HALF_UP)

        return PortfolioInfoDto(
            totalValue = totalValue,
            totalProfit = totalProfit,
            totalProfitPercent = totalProfitPercent,
            stocks = stockDtos
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
        val requestedStock = try {
            investApi.instrumentsService.getShareByUidSync(buyStockRequest.uid)
        } catch (e: Exception) {
            return false
        }
        val interfaceProperties = requestedStock.unknownFields.getField(60).lengthDelimitedList[0].splitIntoStrings()

        val requestedPrice = try {
            investApi.marketDataService.getLastPricesSync(listOf(buyStockRequest.uid)).first()
        } catch (e: Exception) {
            return false
        }

        val user = SecurityContextHolder.getContext().authentication.principal as User
        val portfolio = portfolioRepository.getPortfolioByUser(user)

        var stock = portfolio.stocks.find { it.uid == requestedStock.uid }

        stock?.apply {
            averagePrice =
                recalculateAveragePrice(averagePrice, amount, requestedPrice.price.toBigDecimal(), buyStockRequest.amount)
            amount += buyStockRequest.amount
        } ?: run {
            stock = PortfolioStock(
                portfolio = portfolio,
                uid = requestedStock.uid,
                ticker = requestedStock.ticker,
                name = requestedStock.name,
                amount = buyStockRequest.amount,
                averagePrice = requestedPrice.price.toBigDecimal(),
                logoUrl = interfaceProperties[0].takeWhile { it != '.' },  // remove file extension
                backgroundColor = interfaceProperties[1],
                textColor = interfaceProperties[2],
            )
            portfolio.stocks.add(stock!!)
        }

        portfolioRepository.save(portfolio)
        return true
    }

    fun sellStock(sellStockRequest: SellStockRequest): Boolean {
        val user = SecurityContextHolder.getContext().authentication.principal as User
        val portfolio = portfolioRepository.getPortfolioByUser(user)

        val stock = portfolio.stocks.find { it.uid == sellStockRequest.uid } ?: return false

        if (stock.amount < sellStockRequest.amount) {
            return false
        }

        if (stock.amount == sellStockRequest.amount) {
            stock.portfolio = null
            portfolio.stocks.remove(stock)
        } else {
            val requestedPrice = try {
                investApi.marketDataService.getLastPricesSync(listOf(stock.uid)).first()
            } catch (e: Exception) {
                return false
            }
            stock.averagePrice =
                (stock.averagePrice * stock.amount.toBigDecimal() - requestedPrice.price.toBigDecimal() * sellStockRequest.amount.toBigDecimal()) / (stock.amount.toBigDecimal() - sellStockRequest.amount.toBigDecimal())
            stock.amount -= sellStockRequest.amount
        }

        portfolioRepository.save(portfolio)
        return true
    }
}

private fun calculateTotalValue(stock: PortfolioStock, requestedPrice: LastPrice): BigDecimal {
    return (requestedPrice.price.toBigDecimal() * stock.amount.toBigDecimal()).setScale(2, RoundingMode.HALF_UP)
}

private fun calculateProfit(stock: PortfolioStock, totalValue: BigDecimal): BigDecimal {
    return (totalValue - stock.amount.toBigDecimal() * stock.averagePrice).setScale(2, RoundingMode.HALF_UP)
}

private fun calculateProfitPercent(stock: PortfolioStock, profit: BigDecimal): BigDecimal {
    return (profit / (stock.amount.toBigDecimal() * stock.averagePrice) * BigDecimal(100)).setScale(
        2,
        RoundingMode.HALF_UP
    )
}

private fun recalculateAveragePrice(
    oldAveragePrice: BigDecimal,
    oldAmount: Int,
    newBuyingPrice: BigDecimal,
    newAmount: Int
): BigDecimal {
    val oldAmount = oldAmount.toBigDecimal()
    val newAmount = newAmount.toBigDecimal()
    return (oldAveragePrice * oldAmount + newBuyingPrice * newAmount) / (oldAmount + newAmount).setScale(
        2,
        RoundingMode.HALF_UP
    )
}