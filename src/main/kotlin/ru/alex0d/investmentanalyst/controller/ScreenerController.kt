package ru.alex0d.investmentanalyst.controller

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

    @GetMapping("/gainers")
    fun getGainers(): List<StockChange> {
        val body = makeRequest("https://fmpcloud.io/api/v3/gainers?apikey=$fmpCloudApiKey")
            ?: return emptyList()
        return Json.decodeFromString<List<StockChange>>(body)
    }

    @GetMapping("/losers")
    fun getLosers(): List<StockChange> {
        val body = makeRequest("https://fmpcloud.io/api/v3/losers?apikey=$fmpCloudApiKey")
            ?: return emptyList()
        return Json.decodeFromString<List<StockChange>>(body)
    }

    @GetMapping
    fun getStocks(
        @RequestParam(name = "marketCapMoreThan", required = false) marketCapMoreThan: Long?,
        @RequestParam(name = "marketCapLowerThan", required = false) marketCapLowerThan: Long?,
        @RequestParam(name = "priceMoreThan", required = false) priceMoreThan: Double?,
        @RequestParam(name = "priceLowerThan", required = false) priceLowerThan: Double?,
        @RequestParam(name = "betaMoreThan", required = false) betaMoreThan: Double?,
        @RequestParam(name = "betaLowerThan", required = false) betaLowerThan: Double?,
        @RequestParam(name = "volumeMoreThan", required = false) volumeMoreThan: Long?,
        @RequestParam(name = "volumeLowerThan", required = false) volumeLowerThan: Long?,
        @RequestParam(name = "dividendMoreThan", required = false) dividendMoreThan: Double?,
        @RequestParam(name = "dividendLowerThan", required = false) dividendLowerThan: Double?,
        @RequestParam(name = "isEtf", required = false) isEtf: Boolean?,
        @RequestParam(name = "isActivelyTrading", required = false) isActivelyTrading: Boolean?,
        @RequestParam(name = "sector", required = false) sector: String?,
        @RequestParam(name = "industry", required = false) industry: String?,
        @RequestParam(name = "country", required = false) country: String?,
        @RequestParam(name = "exchange", required = false) exchange: String?,
        @RequestParam(name = "limit", required = false) limit: Int?
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