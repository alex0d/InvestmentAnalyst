package ru.alex0d.investmentanalyst.model

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "users")
class User(
    @Id
    @GeneratedValue
    val id: Int = 0,

    val firstname: String,
    val lastname: String? = null,
    val email: String,
    private val password: String,

    @Enumerated(EnumType.STRING)
    val role: Role,
) : UserDetails {

    override fun getAuthorities() = listOf(GrantedAuthority { "ROLE_${role.name}" })
    override fun getPassword() = password
    override fun getUsername() = email

    override fun isAccountNonExpired() = true
    override fun isAccountNonLocked() = true
    override fun isCredentialsNonExpired() = true
    override fun isEnabled() = true
}

enum class Role {
    USER,
    ADMIN
}
