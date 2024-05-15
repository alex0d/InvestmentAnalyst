package ru.alex0d.investmentanalyst.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.alex0d.investmentanalyst.api.utils.toBigDecimal
import ru.alex0d.investmentanalyst.api.utils.toCandleInterval
import ru.alex0d.investmentanalyst.api.utils.toInstantOfSecond
import ru.alex0d.investmentanalyst.dto.CandlesRequestDto
import ru.alex0d.investmentanalyst.dto.CandlesResponseDto
import ru.tinkoff.piapi.core.InvestApi

@RestController
@CrossOrigin
@RequestMapping("/api/market")
class MarketController(
    private val investApi: InvestApi
) {

    @Operation(summary = "Get candles", description = "Get candles by UID, from, to and interval")
    @ApiResponse(responseCode = "200", description = "The list of candles")
    @PostMapping("/candles")
    fun getCandles(@RequestBody candlesRequest: CandlesRequestDto): ResponseEntity<List<CandlesResponseDto>> {
        val tinkoffCandles = investApi.marketDataService.getCandlesSync(
            candlesRequest.uid,
            candlesRequest.from.toInstantOfSecond(),
            candlesRequest.to.toInstantOfSecond(),
            candlesRequest.interval.toCandleInterval()
        )

        return ResponseEntity.ok(
            tinkoffCandles.map {
                CandlesResponseDto(
                    timestamp = it.time.seconds,
                    open = it.open.toBigDecimal(),
                    close = it.close.toBigDecimal(),
                    high = it.high.toBigDecimal(),
                    low = it.low.toBigDecimal(),
                    volume = it.volume
                )
            }
        )

    }
}