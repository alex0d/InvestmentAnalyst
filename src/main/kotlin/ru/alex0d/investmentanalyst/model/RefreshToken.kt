package ru.alex0d.investmentanalyst.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
@Table(name = "refresh_tokens")
class RefreshToken(
    var token: String,
    var expiration: Long,

    @ManyToOne(cascade = [CascadeType.MERGE])
    @JsonIgnore
    var user: User,

    @Id @GeneratedValue
    var id: Int = 0
)