package ru.alex0d.investmentanalyst.dto

import ru.alex0d.investmentanalyst.model.WatchlistItem

data class CreateWatchlistRequest(
    var title: String
)

data class WatchlistBriefDto(
    var id: Int,
    var title: String,
    var amountOfStocks: Int
)

data class WatchlistDto(
    var id: Int,
    var title: String,
    var items: List<WatchlistItemDto>
)

data class CreateWatchlistItemRequest(
    var ticker: String,
    var lowerPrice: Double?,
    var upperPrice: Double?,
    var comment: String?
)

data class WatchlistItemDto(
    var id: Int,
    var ticker: String,
    var actualPrice: Double,
    var lowerPrice: Double?,
    var upperPrice: Double?,
    var priceReachedRange: Boolean?,
    var comment: String?
) {
    constructor(watchlistItem: WatchlistItem, actualPrice: Double) : this(
        watchlistItem.id,
        watchlistItem.ticker,
        actualPrice,
        watchlistItem.lowerPrice,
        watchlistItem.upperPrice,
        null,
        watchlistItem.comment
    ) {
        if (lowerPrice != null && upperPrice != null) {
            priceReachedRange = actualPrice in lowerPrice!!..upperPrice!!
        }
    }
}