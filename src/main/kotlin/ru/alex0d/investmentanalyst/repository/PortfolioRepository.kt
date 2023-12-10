package ru.alex0d.investmentanalyst.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.alex0d.investmentanalyst.model.Portfolio
import ru.alex0d.investmentanalyst.model.User

@Repository
interface PortfolioRepository : JpaRepository<Portfolio, Int> {
    fun getPortfolioByUser(user: User): Portfolio
}