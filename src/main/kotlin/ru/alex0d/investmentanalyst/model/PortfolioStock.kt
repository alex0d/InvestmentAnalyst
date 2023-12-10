package ru.alex0d.investmentanalyst.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
@Table(name = "portfolio_stocks")
class PortfolioStock(
    @Id @GeneratedValue
    var id: Int = 0,

    @ManyToOne
    @JsonIgnore
    var portfolio: Portfolio?,

    var ticker: String,

    var amount: Int,

    var buyingPrice: Double
)
