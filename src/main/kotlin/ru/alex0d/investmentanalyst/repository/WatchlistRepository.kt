package ru.alex0d.investmentanalyst.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.alex0d.investmentanalyst.model.User
import ru.alex0d.investmentanalyst.model.Watchlist

@Repository
interface WatchlistRepository : JpaRepository<Watchlist, Int> {
    fun getWatchlistsByUser(user: User): List<Watchlist>?
    fun getWatchlistByIdAndUser(watchlistId: Int, user: User): Watchlist?
}