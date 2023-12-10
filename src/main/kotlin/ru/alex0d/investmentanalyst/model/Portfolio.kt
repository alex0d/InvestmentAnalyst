package ru.alex0d.investmentanalyst.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
@Table(name = "portfolios")
class Portfolio(
    @Id @GeneratedValue
    var id: Int = 0,

    @MapsId
    @JoinColumn(name = "id")
    @OneToOne(cascade = [CascadeType.ALL])
    @JsonIgnore
    var user: User? = null,

    @OneToMany(mappedBy = "portfolio", cascade = [CascadeType.ALL])
    var stocks: MutableList<PortfolioStock> = mutableListOf()
)