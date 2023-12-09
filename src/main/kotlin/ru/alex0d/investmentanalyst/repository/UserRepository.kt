package ru.alex0d.investmentanalyst.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.alex0d.investmentanalyst.model.User

interface UserRepository : JpaRepository<User, Int> {
    fun findByEmail(email: String): User?
}