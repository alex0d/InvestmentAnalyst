package ru.alex0d.investmentanalyst.api.fmpcloud

import kotlinx.serialization.Serializable

@Serializable
data class Quote(
    var symbol: String,
    var name: String,
    var price: Double,
    var changesPercentage: Double?,
    var change: Double?,
    var dayLow: Double?,
    var dayHigh: Double?,
    var yearHigh: Double?,
    var yearLow: Double?,
    var marketCap: Long?,
    var priceAvg50: Double?,
    var priceAvg200: Double?,
    var exchange: String?,
    var volume: Long?,
    var avgVolume: Long?,
    var open: Double?,
    var previousClose: Double?,
    var eps: Double?,
    var pe: Double?,
    var earningsAnnouncement: String?,
    var sharesOutstanding: Long?,
    var timestamp: Long?
)
