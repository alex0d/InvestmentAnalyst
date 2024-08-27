package ru.alex0d.investmentanalyst.service

import io.micrometer.core.annotation.Timed
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import ru.alex0d.investmentanalyst.api.utils.splitIntoStrings
import ru.alex0d.investmentanalyst.api.utils.toBigDecimal
import ru.alex0d.investmentanalyst.dto.BuyStockRequest
import ru.alex0d.investmentanalyst.dto.PortfolioInfoDto
import ru.alex0d.investmentanalyst.dto.PortfolioStockInfoDto
import ru.alex0d.investmentanalyst.dto.SellStockRequest
import ru.alex0d.investmentanalyst.model.PortfolioStock
import ru.alex0d.investmentanalyst.model.User
import ru.alex0d.investmentanalyst.repository.PortfolioRepository
import ru.alex0d.investmentanalyst.repository.PortfolioStockRepository
import ru.tinkoff.piapi.core.InvestApi
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime

@Service
class PortfolioService(
    private val portfolioRepository: PortfolioRepository,
    private val portfolioStockRepository: PortfolioStockRepository,
    private val investApi: InvestApi
) {

    @Timed("service_portfolio_getPortfolio")
    fun getPortfolio(): PortfolioInfoDto {
        val user = SecurityContextHolder.getContext().authentication.principal as User
        val stocks = portfolioRepository.getPortfolioByUser(user).stocks

        val uids = stocks.map { it.uid }.distinct()
        val prices = investApi.marketDataService.getLastPricesSync(uids)

        val stockDtos = uids.map { uid ->
            val uidStocks = stocks.filter { it.uid == uid }
            val lastPrice = prices.find { it.instrumentUid == uid }?.price?.toBigDecimal() ?: throw Exception("Price not found")

            val stockSample = uidStocks.first()
            val amount = uidStocks.sumOf { it.amount }
            val averagePrice = uidStocks.sumOf { it.buyingPrice * it.amount.toBigDecimal() } / amount.toBigDecimal()
            PortfolioStockInfoDto(
                uid = uid,
                ticker = stockSample.ticker,
                name = stockSample.name,
                amount = amount,
                averagePrice = averagePrice,
                lastPrice = lastPrice,
                totalValue = lastPrice * amount.toBigDecimal(),
                profit = (lastPrice - averagePrice) * amount.toBigDecimal(),
                profitPercent = ((lastPrice - averagePrice).setScale(4) / averagePrice * BigDecimal(100)).setScale(2, RoundingMode.HALF_UP),
                logoUrl = stockSample.logoUrl,
                backgroundColor = stockSample.backgroundColor,
                textColor = stockSample.textColor
            )
        }

        val totalValue = stockDtos.sumOf { it.totalValue }.setScale(2, RoundingMode.HALF_UP)
        val totalProfit = stockDtos.sumOf { it.profit }.setScale(2, RoundingMode.HALF_UP)
        val totalProfitPercent =
            (if (totalValue.compareTo(BigDecimal.ZERO) != 0) totalProfit.setScale(4) / totalValue * BigDecimal(100) else BigDecimal.ZERO)
                .setScale(2, RoundingMode.HALF_UP)

        return PortfolioInfoDto(
            totalValue = totalValue,
            totalProfit = totalProfit,
            totalProfitPercent = totalProfitPercent,
            stocks = stockDtos
        )
    }

    @Timed("service_portfolio_buyStock")
    fun buyStock(buyStockRequest: BuyStockRequest): Boolean {
        val requestedStock = try {
            investApi.instrumentsService.getShareByUidSync(buyStockRequest.uid)
        } catch (e: Exception) {
            return false
        }
        val interfaceProperties = requestedStock.unknownFields.getField(60).lengthDelimitedList[0].splitIntoStrings()

        val lastPrice = try {
            investApi.marketDataService.getLastPricesSync(listOf(buyStockRequest.uid)).first().price.toBigDecimal()
        } catch (e: Exception) {
            return false
        }

        val user = SecurityContextHolder.getContext().authentication.principal as User
        val portfolio = portfolioRepository.getPortfolioByUser(user)

        val stock = PortfolioStock(
            portfolio = portfolio,
            uid = requestedStock.uid,
            ticker = requestedStock.ticker,
            name = requestedStock.name,
            amount = buyStockRequest.amount,
            buyingPrice = lastPrice,
            buyingTime = LocalDateTime.now(),
            logoUrl = interfaceProperties[0].takeWhile { it != '.' },  // remove file extension
            backgroundColor = interfaceProperties[1],
            textColor = interfaceProperties[2],
        )

        portfolio.stocks.add(stock)
        portfolioRepository.save(portfolio)
        return true
    }

    @Timed("service_portfolio_sellStock")
    fun sellStock(sellStockRequest: SellStockRequest): Boolean {
        val user = SecurityContextHolder.getContext().authentication.principal as User
        val portfolio = portfolioRepository.getPortfolioByUser(user)

        val stocks = portfolio.stocks.filter { it.uid == sellStockRequest.uid }
        if (stocks.isEmpty()) return false

        var amount = sellStockRequest.amount
        val stocksToBeDeleted = mutableListOf<PortfolioStock>()

        for (stock in stocks.sortedBy { it.buyingTime }) {
            if (amount == 0) break

            if (stock.amount > amount) {
                stock.amount -= amount
                amount = 0
            } else {
                amount -= stock.amount
                stocksToBeDeleted.add(stock)
                portfolio.stocks.remove(stock)
            }
        }
        if (amount != 0) return false

        portfolioRepository.save(portfolio)
        portfolioStockRepository.deleteAll(stocksToBeDeleted)
        return true
    }
}