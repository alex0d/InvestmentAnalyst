package ru.alex0d.investmentanalyst.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.tinkoff.piapi.core.InvestApi

@Configuration
class TinkoffApiConfig {

    @Bean
    fun tinkoffInvestApi(@Value("\${application.tinkoff.key}") token: String): InvestApi {
        return InvestApi.create(token)
    }
}
