package ru.alex0d.investmentanalyst.service

import kotlinx.serialization.json.Json
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import ru.alex0d.investmentanalyst.api.fmpcloud.Quote
import ru.alex0d.investmentanalyst.api.makeRequest
import ru.alex0d.investmentanalyst.dto.CreateWatchlistItemRequest
import ru.alex0d.investmentanalyst.dto.WatchlistDto
import ru.alex0d.investmentanalyst.dto.WatchlistItemDto
import ru.alex0d.investmentanalyst.model.User
import ru.alex0d.investmentanalyst.model.Watchlist
import ru.alex0d.investmentanalyst.model.WatchlistItem
import ru.alex0d.investmentanalyst.repository.WatchlistItemRepository
import ru.alex0d.investmentanalyst.repository.WatchlistRepository

@Service
class WatchlistService(
    private val watchlistRepository: WatchlistRepository,
    private val watchlistItemRepository: WatchlistItemRepository
) {
    @Value("\${application.fmpcloud.key}")
    private lateinit var fmpCloudApiKey: String

    fun getWatchlists(): List<Watchlist>? {
        val user = SecurityContextHolder.getContext().authentication.principal as User
        return watchlistRepository.getWatchlistsByUser(user)
    }

    fun getWatchlist(watchlistId: Int, withPriceInRangeOnly: Boolean = false): WatchlistDto? {
        val user = SecurityContextHolder.getContext().authentication.principal as User
        val watchlist = watchlistRepository.getWatchlistByIdAndUser(watchlistId, user)
            ?: return null

        val watchlistDto = WatchlistDto(
            watchlist.id,
            watchlist.title,
            watchlist.items.map {
                val body = makeRequest("https://fmpcloud.io/api/v3/quote/${it.ticker}?apikey=$fmpCloudApiKey")
                    ?: throw Exception("Empty response body")
                val requestedStock = Json.decodeFromString<List<Quote>>(body)[0]

                WatchlistItemDto(it, requestedStock.price)
            }
        )
        if (withPriceInRangeOnly) {
            watchlistDto.items = watchlistDto.items.filter { it.priceReachedRange == true }
        }
        return watchlistDto
    }

    fun createWatchlist(title: String): Watchlist {
        val user = SecurityContextHolder.getContext().authentication.principal as User
        val watchlist = Watchlist(
            title = title,
            user = user
        )
        return watchlistRepository.save(watchlist)
    }

    fun deleteWatchlist(watchlistId: Int) {
        val user = SecurityContextHolder.getContext().authentication.principal as User
        val watchlist = watchlistRepository.getWatchlistByIdAndUser(watchlistId, user)
            ?: throw Exception("Watchlist not found")
        watchlist.user = null
        watchlistRepository.delete(watchlist)
    }

    fun addTickerToWatchlist(watchlistId: Int, request: CreateWatchlistItemRequest): WatchlistItemDto? {
        val user = SecurityContextHolder.getContext().authentication.principal as User
        val watchlist = watchlistRepository.getWatchlistByIdAndUser(watchlistId, user)
            ?: return null

        val watchlistItem = watchlist.items.find { it.ticker == request.ticker }
        if (watchlistItem != null) {
            return null
        }

        val watchlistItemToSave = WatchlistItem(
            ticker = request.ticker,
            lowerPrice = request.lowerPrice,
            upperPrice = request.upperPrice,
            comment = request.comment,
            watchlist = watchlist
        )
        watchlist.items.add(watchlistItemToSave)
        val savedWatchlistItem = watchlistItemRepository.save(watchlistItemToSave)
        watchlistRepository.save(watchlist)

        val body = makeRequest("https://fmpcloud.io/api/v3/quote/${savedWatchlistItem.ticker}?apikey=$fmpCloudApiKey")
            ?: throw Exception("Empty response body")
        val requestedStock = Json.decodeFromString<List<Quote>>(body)[0]

        return WatchlistItemDto(savedWatchlistItem, requestedStock.price)
    }

    fun removeTickerFromWatchlist(watchlistId: Int, watchlistItemId: Int) {
        val user = SecurityContextHolder.getContext().authentication.principal as User
        val watchlist = watchlistRepository.getWatchlistByIdAndUser(watchlistId, user)
            ?: throw Exception("Watchlist not found")

        val watchlistItem = watchlist.items.find { it.id == watchlistItemId }
            ?: throw Exception("Watchlist item not found")

        watchlistItem.watchlist = null
        watchlistItemRepository.delete(watchlistItem)
    }

}