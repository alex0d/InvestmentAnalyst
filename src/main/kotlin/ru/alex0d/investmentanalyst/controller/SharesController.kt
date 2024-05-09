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
@RequestMapping("/api/share")
class SharesController(
    private val investApi: InvestApi
) {

    @Operation(summary = "Get share by UID", description = "Get share by UID")
    @ApiResponse(responseCode = "200", description = "The share retrieved successfully")
    @GetMapping("/{uid}")
    fun getShareByUid(@Parameter(description = "UID") @PathVariable uid: String): ResponseEntity<TinkoffShare> {
        val lastPrice = investApi.marketDataService.getLastPricesSync(listOf(uid)).first().price.toBigDecimal()

        return ResponseEntity.ok(
            TinkoffShare(share = investApi.instrumentsService.getShareByUidSync(uid), lastPrice = lastPrice)
        )
    }
}