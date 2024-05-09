package ru.alex0d.investmentanalyst.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.alex0d.investmentanalyst.model.RefreshToken

@Repository
interface RefreshTokenRepository : JpaRepository<RefreshToken, Int> {
    fun findByToken(token: String): RefreshToken?
}