package ru.alex0d.investmentanalyst.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirements
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.alex0d.investmentanalyst.dto.AuthenticationRequest
import ru.alex0d.investmentanalyst.dto.AuthenticationResponse
import ru.alex0d.investmentanalyst.dto.RefreshTokenRequest
import ru.alex0d.investmentanalyst.dto.RegisterRequest
import ru.alex0d.investmentanalyst.service.AuthenticationService
import ru.alex0d.investmentanalyst.service.AuthenticationStatus

@RestController
@CrossOrigin
@RequestMapping("/api/auth")
@SecurityRequirements  // Disable Swagger UI security for this controller
class AuthenticationController(
    private val authenticationService: AuthenticationService
) {

    @Operation(summary = "Register a new user")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "User registered successfully"),
            ApiResponse(responseCode = "409", description = "User with specified email already exists")
        ]
    )
    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<AuthenticationResponse> {
        authenticationService.register(request)?.let {
            return ResponseEntity.ok(it)
        } ?: return ResponseEntity.status(409).build()
    }

    @Operation(summary = "Authenticate a user")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "User authenticated successfully"),
            ApiResponse(responseCode = "401", description = "Password incorrect"),
            ApiResponse(responseCode = "422", description = "User not found")
        ]
    )
    @PostMapping("/authenticate")
    fun authenticate(@RequestBody request: AuthenticationRequest): ResponseEntity<AuthenticationResponse> {
        val authResult = authenticationService.authenticate(request)
        return when (authResult.first) {
            AuthenticationStatus.OK -> ResponseEntity.ok(authResult.second!!)
            AuthenticationStatus.USER_NOT_FOUND -> ResponseEntity.status(422).build()
            AuthenticationStatus.PASSWORD_INCORRECT -> ResponseEntity.status(401).build()
        }
    }

    @Operation(summary = "Refresh the access token")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Token refreshed successfully"),
            ApiResponse(responseCode = "401", description = "Invalid refresh token")
        ]
    )
    @PostMapping("/refresh")
    fun refresh(@RequestBody request: RefreshTokenRequest): ResponseEntity<AuthenticationResponse> {
        val response = authenticationService.refresh(request)
        return if (response != null) {
            ResponseEntity.ok(response)
        } else {
            ResponseEntity.status(401).build()
        }
    }
}