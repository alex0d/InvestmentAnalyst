package ru.alex0d.investmentanalyst.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "portfolio_stocks")
class PortfolioStock(
    @ManyToOne
    @JsonIgnore
    var portfolio: Portfolio?,

    var uid: String,
    var ticker: String,
    var name: String,

    var amount: Int,
    var buyingPrice: BigDecimal,
    var buyingTime: LocalDateTime,

    var logoUrl: String,
    var backgroundColor: String,
    var textColor: String,

    @Id @GeneratedValue
    var id: Int = 0
)
