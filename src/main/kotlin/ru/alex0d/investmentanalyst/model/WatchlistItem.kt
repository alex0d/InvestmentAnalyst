package ru.alex0d.investmentanalyst.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
@Table(name = "watchlist_items")
class WatchlistItem(
    @ManyToOne
    @JsonIgnore
    var watchlist: Watchlist?,

    var ticker: String,

    var lowerPrice: Double?,
    var upperPrice: Double?,

    var comment: String?,

    @Id @GeneratedValue
    var id: Int = 0
)