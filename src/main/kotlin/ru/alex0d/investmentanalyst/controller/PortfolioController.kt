package ru.alex0d.investmentanalyst.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.alex0d.investmentanalyst.api.finage.MarketNews
import ru.alex0d.investmentanalyst.dto.BuyStockRequest
import ru.alex0d.investmentanalyst.dto.PortfolioInfoDto
import ru.alex0d.investmentanalyst.dto.SellStockRequest
import ru.alex0d.investmentanalyst.service.PortfolioService

@RestController
@CrossOrigin
@RequestMapping("/api/portfolio")
class PortfolioController(
    private val portfolioService: PortfolioService
) {

    @Operation(summary = "Get portfolio info")
    @ApiResponse(responseCode = "200", description = "Portfolio info retrieved successfully")
    @GetMapping
    fun getPortfolio(): ResponseEntity<PortfolioInfoDto> {
        val portfolio = portfolioService.getPortfolio()
        return ResponseEntity.ok(portfolio)
    }

    @Operation(summary = "Get portfolio news", description = "Get news about stocks in portfolio")
    @ApiResponse(responseCode = "200", description = "Portfolio news retrieved successfully")
    @GetMapping("/news")
    fun getPortfolioNews(): ResponseEntity<List<MarketNews>> {
        val news = portfolioService.getPortfolioNews()
        return ResponseEntity.ok(news)
    }

    @Operation(summary = "Buy stock", description = "Buy stock and add it to portfolio. If stock is already in portfolio, amount will be increased and average price will be recalculated")
    @ApiResponse(responseCode = "200", description = "Stock bought successfully")
    @PostMapping("/buy")
    fun buyStock(@RequestBody buyStockRequest: BuyStockRequest): ResponseEntity<String> {
        return if (buyStockRequest.amount <= 0) {
            ResponseEntity.badRequest().body("Amount must be positive")
        } else if (buyStockRequest.uid.isBlank()) {
            ResponseEntity.badRequest().body("Ticker must not be blank")
        } else if (portfolioService.buyStock(buyStockRequest)) {
            ResponseEntity.ok("Stock bought")
        } else {
            ResponseEntity.badRequest().body("Error buying stock")
        }
    }

    @Operation(summary = "Sell stock", description = "Sell stock and remove it from portfolio. If not all stocks are sold, amount will be decreased and average price will be recalculated")
    @ApiResponse(responseCode = "200", description = "Stock sold successfully")
    @PostMapping("/sell")
    fun sellStock(@RequestBody sellStockRequest: SellStockRequest): ResponseEntity<String> {
        return if (sellStockRequest.amount <= 0) {
            ResponseEntity.badRequest().body("Amount must be positive")
        } else if (sellStockRequest.uid.isBlank()) {
            ResponseEntity.badRequest().body("Ticker must not be blank")
        } else if (portfolioService.sellStock(sellStockRequest)) {
            ResponseEntity.ok("Stock sold")
        } else {
            ResponseEntity.badRequest().body("Error selling stock")
        }
    }
}