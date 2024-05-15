package ru.alex0d.investmentanalyst.api.utils

import ru.tinkoff.piapi.contract.v1.CandleInterval
import java.time.Instant

fun String.toCandleInterval(): CandleInterval {
    return when (this) {
        "1min" -> CandleInterval.CANDLE_INTERVAL_1_MIN
        "2min" -> CandleInterval.CANDLE_INTERVAL_2_MIN
        "3min" -> CandleInterval.CANDLE_INTERVAL_3_MIN
        "5min" -> CandleInterval.CANDLE_INTERVAL_5_MIN
        "10min" -> CandleInterval.CANDLE_INTERVAL_10_MIN
        "15min" -> CandleInterval.CANDLE_INTERVAL_15_MIN
        "30min" -> CandleInterval.CANDLE_INTERVAL_30_MIN
        "hour" -> CandleInterval.CANDLE_INTERVAL_HOUR
        "2hour" -> CandleInterval.CANDLE_INTERVAL_2_HOUR
        "4hour" -> CandleInterval.CANDLE_INTERVAL_4_HOUR
        "day" -> CandleInterval.CANDLE_INTERVAL_DAY
        "week" -> CandleInterval.CANDLE_INTERVAL_WEEK
        "month" -> CandleInterval.CANDLE_INTERVAL_MONTH
        else -> CandleInterval.CANDLE_INTERVAL_UNSPECIFIED
    }
}

fun Long.toInstantOfSecond(): Instant {
    return Instant.ofEpochSecond(this)
}