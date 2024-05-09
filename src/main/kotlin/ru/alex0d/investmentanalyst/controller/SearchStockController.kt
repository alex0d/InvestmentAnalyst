package ru.alex0d.investmentanalyst.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirements
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.alex0d.investmentanalyst.dto.TinkoffInstrumentShort
import ru.tinkoff.piapi.core.InvestApi

@RestController
@CrossOrigin
@RequestMapping("/api/search")
@SecurityRequirements  // Disable Swagger UI security for this controller
class SearchStockController(
    private val investApi: InvestApi
) {

    @Operation(summary = "Get shares", description = "Get shares by ticker")
    @ApiResponse(responseCode = "200", description = "Shares retrieved successfully")
    @GetMapping("/shares/{ticker}")
    fun getShares(@Parameter(description = "Ticker") @PathVariable ticker: String): ResponseEntity<List<TinkoffInstrumentShort>> {
        return ResponseEntity.ok(
            investApi.instrumentsService.findInstrumentSync(ticker)
                .filter { it.instrumentType == "share" && it.apiTradeAvailableFlag }
                .map { TinkoffInstrumentShort(it) }
        )
    }
}