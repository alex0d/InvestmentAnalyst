package ru.alex0d.investmentanalyst.dto

import ru.tinkoff.piapi.contract.v1.Share
import java.math.BigDecimal

data class TinkoffShare(
    val uid: String,
    val figi: String,
    val ticker: String,
    val classCode: String,
    val isin: String,
    val currency: String,
    val name: String,
    val countryOfRisk: String,
    val countryOfRiskName: String,
    val sector: String,
    val lot: Int,
    var lastPrice: BigDecimal = BigDecimal.ZERO,
    var url: String = "",
    var backgroundColor: String = "#ffffff",
    var textColor: String = "#000000",
) {
    constructor(share: Share, lastPrice: BigDecimal) : this(
        uid = share.uid,
        figi = share.figi,
        ticker = share.ticker,
        classCode = share.classCode,
        isin = share.isin,
        currency = share.currency,
        name = share.name,
        countryOfRisk = share.countryOfRisk,
        countryOfRiskName = share.countryOfRiskName,
        sector = share.sector,
        lot = share.lot,
        lastPrice = lastPrice,
        url = share.brand.logoName,
        backgroundColor = share.brand.logoBaseColor,
        textColor = share.brand.textColor
    )
}