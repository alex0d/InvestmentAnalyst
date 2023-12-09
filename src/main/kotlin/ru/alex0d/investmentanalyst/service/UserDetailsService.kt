package ru.alex0d.investmentanalyst.service

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import ru.alex0d.investmentanalyst.repository.UserRepository

@Service
class UserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        val user = userRepository.findByEmail(username!!)
        return user ?: throw UsernameNotFoundException("User not found")
    }
}