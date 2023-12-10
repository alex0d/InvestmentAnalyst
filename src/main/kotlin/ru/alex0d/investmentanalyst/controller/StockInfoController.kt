package ru.alex0d.investmentanalyst.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import kotlinx.serialization.json.Json
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.alex0d.investmentanalyst.api.fmpcloud.Quote
import ru.alex0d.investmentanalyst.api.fmpcloud.Rating
import ru.alex0d.investmentanalyst.api.makeRequest

@RestController
@RequestMapping("/api")
class StockInfoController {
    @Value("\${application.fmpcloud.key}")
    private lateinit var fmpCloudApiKey: String

    @Operation(summary = "Get quote", description = "Get quote for a stock")
    @ApiResponse(responseCode = "200", description = "Quote retrieved successfully")
    @GetMapping("/quote/{ticker}")
    fun getQuote(@PathVariable ticker: String): ResponseEntity<Quote> {
        val body = makeRequest("https://fmpcloud.io/api/v3/quote/$ticker?apikey=$fmpCloudApiKey")
            ?: return ResponseEntity.badRequest().build()
        val quote = Json.decodeFromString<List<Quote>>(body)[0]
        return ResponseEntity.ok(quote)
    }

    @Operation(summary = "Get rating", description = "Get info from rating companies")
    @ApiResponse(responseCode = "200", description = "Rating retrieved successfully")
    @GetMapping("/rating/{ticker}")
    fun getRating(@PathVariable ticker: String): ResponseEntity<Rating> {
        val body = makeRequest("https://fmpcloud.io/api/v3/rating/$ticker?apikey=$fmpCloudApiKey")
            ?: return ResponseEntity.badRequest().build()
        val rating = Json.decodeFromString<List<Rating>>(body)[0]
        return ResponseEntity.ok(rating)
    }
}