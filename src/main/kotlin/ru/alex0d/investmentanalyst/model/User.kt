package ru.alex0d.investmentanalyst.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "users")
class User(
    @Id @GeneratedValue
    var id: Int = 0,

    var firstname: String,
    var lastname: String? = null,
    var email: String,
    private val password: String,

    @Enumerated(EnumType.STRING)
    var role: Role,

    @OneToOne(mappedBy = "user", cascade = [CascadeType.ALL])
    @JsonIgnore
    var portfolio: Portfolio,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var watchlists: MutableList<Watchlist> = mutableListOf()
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
