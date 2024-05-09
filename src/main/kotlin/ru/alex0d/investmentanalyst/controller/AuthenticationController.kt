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

@RestController
@CrossOrigin
@RequestMapping("/api/auth")
@SecurityRequirements  // Disable Swagger UI security for this controller
class AuthenticationController(
    private val authenticationService: AuthenticationService
) {

    @Operation(summary = "Register a new user")
    @ApiResponse(responseCode = "200", description = "User registered successfully")
    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<AuthenticationResponse> {
        return ResponseEntity.ok(authenticationService.register(request))
    }

    @Operation(summary = "Authenticate a user")
    @ApiResponse(responseCode = "200", description = "User authenticated successfully")
    @PostMapping("/authenticate")
    fun register(@RequestBody request: AuthenticationRequest): ResponseEntity<AuthenticationResponse> {
        return ResponseEntity.ok(authenticationService.authenticate(request))
    }

    @Operation(summary = "Refresh the access token")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Token refreshed successfully"),
            ApiResponse(responseCode = "400", description = "Bad request")
        ]
    )
    @PostMapping("/refresh")
    fun refresh(@RequestBody request: RefreshTokenRequest): ResponseEntity<AuthenticationResponse> {
        val response = authenticationService.refresh(request)
        return if (response != null) {
            ResponseEntity.ok(response)
        } else {
            ResponseEntity.badRequest().build()
        }
    }
}