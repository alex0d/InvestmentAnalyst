package ru.alex0d.investmentanalyst.config

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.SecretKey

@Service
class JwtService {
    @Value("\${application.security.jwt.secret-key}")
    private lateinit var secretKey: String

    @Value("\${application.security.jwt.expiration}")
    private var jwtExpiration: Long = 1000 * 60 * 60 * 10  // 10 hours

    fun generateToken(userDetails: UserDetails): String =
        generateToken(emptyMap(), userDetails)

    fun generateToken(extraClaims: Map<String, Any>, userDetails: UserDetails): String =
        Jwts.builder()
            .claims(extraClaims)
            .subject(userDetails.username)
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(Date(System.currentTimeMillis() + jwtExpiration))
            .signWith(getSignInKey(), Jwts.SIG.HS256)
            .compact()

    fun isTokenValid(token: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return username == userDetails.username && !isTokenExpired(token)
    }

    fun isTokenExpired(token: String) = extractExpiration(token)?.before(Date()) ?: true

    fun extractExpiration(token: String) = extractClaim(token) { it.expiration }

    fun extractUsername(token: String) = extractClaim(token) { it.subject }

    fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T?): T? {
        val claims = extractAllClaims(token)
        return claimsResolver(claims)
    }

    private fun extractAllClaims(jwt: String): Claims =
        Jwts.parser()
            .verifyWith(getSignInKey())
            .build()
            .parseSignedClaims(jwt)
            .payload

    private fun getSignInKey(): SecretKey {
        val keyBytes = Decoders.BASE64.decode(secretKey)
        return Keys.hmacShaKeyFor(keyBytes)
    }
}
