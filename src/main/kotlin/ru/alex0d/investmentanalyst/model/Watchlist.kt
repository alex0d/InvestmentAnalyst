package ru.alex0d.investmentanalyst.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
@Table(name = "watchlists")
class Watchlist(
    @Id @GeneratedValue
    var id: Int = 0,

    var title: String,

    @ManyToOne(cascade = [CascadeType.MERGE])
    @JsonIgnore
    var user: User? = null,

    @OneToMany(mappedBy = "watchlist", cascade = [CascadeType.ALL])
    var items: MutableList<WatchlistItem> = mutableListOf()
)