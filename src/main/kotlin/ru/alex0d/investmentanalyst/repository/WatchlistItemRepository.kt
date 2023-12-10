package ru.alex0d.investmentanalyst.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.alex0d.investmentanalyst.model.WatchlistItem

interface WatchlistItemRepository : JpaRepository<WatchlistItem, Int>