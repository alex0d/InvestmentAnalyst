package ru.alex0d.investmentanalyst.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.alex0d.investmentanalyst.model.PortfolioStock

@Repository
interface PortfolioStockRepository : JpaRepository<PortfolioStock, Int>