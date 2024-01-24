package ru.alex0d.investmentanalyst.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
@Table(name = "portfolio_stocks")
class PortfolioStock(
    @ManyToOne
    @JsonIgnore
    var portfolio: Portfolio?,

    var ticker: String,

    var amount: Int,

    var buyingPrice: Double,

    @Id @GeneratedValue
    var id: Int = 0
)
