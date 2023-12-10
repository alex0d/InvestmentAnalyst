package ru.alex0d.investmentanalyst.api.fmpcloud

import kotlinx.serialization.Serializable

@Serializable
data class StockChange(
    var ticker: String,
    var changes: Double?,
    var price: String?,
    var changesPercentage: String?,
    var companyName: String?
)