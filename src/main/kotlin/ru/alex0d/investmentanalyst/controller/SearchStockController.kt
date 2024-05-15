package ru.alex0d.investmentanalyst.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.alex0d.investmentanalyst.api.utils.toBigDecimal
import ru.alex0d.investmentanalyst.dto.TinkoffShare
import ru.tinkoff.piapi.core.InvestApi

@RestController
@CrossOrigin
@RequestMapping("/api/search")
class SearchStockController(
    private val investApi: InvestApi
) {

    @Operation(summary = "Get shares", description = "Get shares by ticker")
    @ApiResponse(responseCode = "200", description = "Shares retrieved successfully")
    @GetMapping("/shares/{ticker}")
    fun getShares(@Parameter(description = "Ticker") @PathVariable ticker: String): ResponseEntity<List<TinkoffShare>> {
        return ResponseEntity.ok(
            investApi.instrumentsService.findInstrumentSync(ticker)
                .filter { it.instrumentType == "share" && it.apiTradeAvailableFlag }
                .take(20)
                .map {
                    val lastPrice = investApi.marketDataService.getLastPricesSync(listOf(it.uid)).first().price.toBigDecimal()
                    TinkoffShare(share = investApi.instrumentsService.getShareByUidSync(it.uid), lastPrice = lastPrice)
                }
                .filter { it.currency == "rub" }
        )
    }
}