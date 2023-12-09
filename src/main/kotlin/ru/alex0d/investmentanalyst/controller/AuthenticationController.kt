package ru.alex0d.investmentanalyst.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.alex0d.investmentanalyst.dto.AuthenticationRequest
import ru.alex0d.investmentanalyst.dto.AuthenticationResponse
import ru.alex0d.investmentanalyst.dto.RegisterRequest
import ru.alex0d.investmentanalyst.service.AuthenticationService

@RestController
@RequestMapping("/api/auth")
class AuthenticationController(
    private val authenticationService: AuthenticationService
) {

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<AuthenticationResponse> {
        return ResponseEntity.ok(authenticationService.register(request))
    }

    @PostMapping("/authenticate")
    fun register(@RequestBody request: AuthenticationRequest): ResponseEntity<AuthenticationResponse> {
        return ResponseEntity.ok(authenticationService.authenticate(request))
    }
}