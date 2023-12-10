package ru.alex0d.investmentanalyst.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import kotlinx.serialization.json.Json
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.alex0d.investmentanalyst.api.fmpcloud.StockChange
import ru.alex0d.investmentanalyst.api.makeRequest

@RestController
@RequestMapping("/api/screener")
class ScreenerController {
    @Value("\${application.fmpcloud.key}")
    private lateinit var fmpCloudApiKey: String

    @Operation(summary = "Get gainers", description = "Get stocks with biggest price increase")
    @ApiResponse(responseCode = "200", description = "Gainers retrieved successfully")
    @GetMapping("/gainers")
    fun getGainers(): List<StockChange> {
        val body = makeRequest("https://fmpcloud.io/api/v3/gainers?apikey=$fmpCloudApiKey")
            ?: return emptyList()
        return Json.decodeFromString<List<StockChange>>(body)
    }

    @Operation(summary = "Get losers", description = "Get stocks with biggest price decrease")
    @ApiResponse(responseCode = "200", description = "Losers retrieved successfully")
    @GetMapping("/losers")
    fun getLosers(): List<StockChange> {
        val body = makeRequest("https://fmpcloud.io/api/v3/losers?apikey=$fmpCloudApiKey")
            ?: return emptyList()
        return Json.decodeFromString<List<StockChange>>(body)
    }

    @Operation(summary = "Get a selection of stocks", description = "Get a selection of stocks based on different criteria")
    @ApiResponse(responseCode = "200", description = "Stocks retrieved successfully")
    @GetMapping
    fun getStocks(
        @Parameter(example = "1000000000") @RequestParam(name = "marketCapMoreThan", required = false) marketCapMoreThan: Long?,
        @Parameter(example = "1000000000") @RequestParam(name = "marketCapLowerThan", required = false) marketCapLowerThan: Long?,
        @Parameter(example = "100") @RequestParam(name = "priceMoreThan", required = false) priceMoreThan: Double?,
        @Parameter(example = "100") @RequestParam(name = "priceLowerThan", required = false) priceLowerThan: Double?,
        @Parameter(example = "1") @RequestParam(name = "betaMoreThan", required = false) betaMoreThan: Double?,
        @Parameter(example = "1") @RequestParam(name = "betaLowerThan", required = false) betaLowerThan: Double?,
        @Parameter(example = "10000") @RequestParam(name = "volumeMoreThan", required = false) volumeMoreThan: Long?,
        @Parameter(example = "10000") @RequestParam(name = "volumeLowerThan", required = false) volumeLowerThan: Long?,
        @Parameter(example = "1") @RequestParam(name = "dividendMoreThan", required = false) dividendMoreThan: Double?,
        @Parameter(example = "1") @RequestParam(name = "dividendLowerThan", required = false) dividendLowerThan: Double?,
        @Parameter(example = "true") @RequestParam(name = "isEtf", required = false) isEtf: Boolean?,
        @Parameter(example = "true") @RequestParam(name = "isActivelyTrading", required = false) isActivelyTrading: Boolean?,
        @Parameter(example = "Technology") @RequestParam(name = "sector", required = false) sector: String?,
        @Parameter(example = "Software") @RequestParam(name = "industry", required = false) industry: String?,
        @Parameter(example = "US") @RequestParam(name = "country", required = false) country: String?,
        @Parameter(example = "nasdaq") @RequestParam(name = "exchange", required = false) exchange: String?,
        @Parameter(example = "10") @RequestParam(name = "limit", required = false) limit: Int?
    ): String {
        val url = "https://fmpcloud.io/api/v3/stock-screener?apikey=$fmpCloudApiKey" +
                (marketCapMoreThan?.let { "&marketCapMoreThan=$it" } ?: "") +
                (marketCapLowerThan?.let { "&marketCapLowerThan=$it" } ?: "") +
                (priceMoreThan?.let { "&priceMoreThan=$it" } ?: "") +
                (priceLowerThan?.let { "&priceLowerThan=$it" } ?: "") +
                (betaMoreThan?.let { "&betaMoreThan=$it" } ?: "") +
                (betaLowerThan?.let { "&betaLowerThan=$it" } ?: "") +
                (volumeMoreThan?.let { "&volumeMoreThan=$it" } ?: "") +
                (volumeLowerThan?.let { "&volumeLowerThan=$it" } ?: "") +
                (dividendMoreThan?.let { "&dividendMoreThan=$it" } ?: "") +
                (dividendLowerThan?.let { "&dividendLowerThan=$it" } ?: "") +
                (isEtf?.let { "&isEtf=$it" } ?: "") +
                (isActivelyTrading?.let { "&isActivelyTrading=$it" } ?: "") +
                (sector?.let { "&sector=$it" } ?: "") +
                (industry?.let { "&industry=$it" } ?: "") +
                (country?.let { "&country=$it" } ?: "") +
                (exchange?.let { "&exchange=$it" } ?: "") +
                (limit?.let { "&limit=$it" } ?: "")
        return makeRequest(url) ?: ""
    }
}