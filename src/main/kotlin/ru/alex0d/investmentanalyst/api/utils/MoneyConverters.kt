package ru.alex0d.investmentanalyst.api.utils

import ru.tinkoff.piapi.contract.v1.Quotation
import java.math.BigDecimal
import java.math.RoundingMode

fun Quotation.toBigDecimal(): BigDecimal {
    return BigDecimal.valueOf(this.units).add(BigDecimal.valueOf(this.nano.toLong(), 9)).setScale(2, RoundingMode.HALF_UP)
}

fun Quotation.toDouble(): Double {
    return BigDecimal.valueOf(this.units).add(BigDecimal.valueOf(this.nano.toLong(), 9)).toDouble()
}