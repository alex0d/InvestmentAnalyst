package ru.alex0d.investmentanalyst.dto

import ru.alex0d.investmentanalyst.api.utils.splitIntoStrings
import ru.tinkoff.piapi.contract.v1.InstrumentShort
import ru.tinkoff.piapi.contract.v1.Share

data class TinkoffInstrumentShort(
    val uid: String,
    val figi: String,
    val isin: String,
    val ticker: String,
    val classCode: String,
    val name: String,
    val instrumentType: String,
) {
    constructor(instrumentShort: InstrumentShort) : this(
        instrumentShort.uid,
        instrumentShort.figi,
        instrumentShort.isin,
        instrumentShort.ticker,
        instrumentShort.classCode,
        instrumentShort.name,
        instrumentShort.instrumentType,
    )
}

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
    var url: String = "",
    var textColor: String = "#000000",
    var backgroundColor: String = "#ffffff",
) {
    constructor(share: Share) : this(
        share.uid,
        share.figi,
        share.ticker,
        share.classCode,
        share.isin,
        share.currency,
        share.name,
        share.countryOfRisk,
        share.countryOfRiskName,
        share.sector
    ) {
        val interfaceProperties = share.unknownFields.getField(60).lengthDelimitedList[0].splitIntoStrings()
        url = interfaceProperties[0].takeWhile { it != '.' }
        textColor = interfaceProperties[1]
        backgroundColor = interfaceProperties[2]
    }
}