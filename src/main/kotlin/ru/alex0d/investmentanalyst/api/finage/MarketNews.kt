package ru.alex0d.investmentanalyst.api.finage

import kotlinx.serialization.Serializable

@Serializable
data class MarketNews(
    var ticker: String,
    var limit: Int,
    var news: ArrayList<News>,
)

@Serializable
data class News(
    var source: String?,
    var title: String?,
    var description: String?,
    var url: String?,
    var date: String?
)
