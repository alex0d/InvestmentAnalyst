package ru.alex0d.investmentanalyst.service

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import ru.alex0d.investmentanalyst.config.JwtService
import ru.alex0d.investmentanalyst.dto.AuthenticationRequest
import ru.alex0d.investmentanalyst.dto.AuthenticationResponse
import ru.alex0d.investmentanalyst.dto.RegisterRequest
import ru.alex0d.investmentanalyst.model.Role
import ru.alex0d.investmentanalyst.model.User
import ru.alex0d.investmentanalyst.repository.UserRepository

@Service
class AuthenticationService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager
) {
    fun register(request: RegisterRequest): AuthenticationResponse {
        val user = User(
            firstname = request.firstname,
            lastname = request.lastname,
            email = request.email,
            password = passwordEncoder.encode(request.password),
            role = Role.USER
        )
        userRepository.save(user)
        val jwtToken = jwtService.generateToken(user)
        return AuthenticationResponse(jwtToken)
    }

    fun authenticate(request: AuthenticationRequest): AuthenticationResponse {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                request.email,
                request.password
            )
        )
        val user = userRepository.findByEmail(request.email)!!
        val jwtToken = jwtService.generateToken(user)
        return AuthenticationResponse(jwtToken)
    }
}