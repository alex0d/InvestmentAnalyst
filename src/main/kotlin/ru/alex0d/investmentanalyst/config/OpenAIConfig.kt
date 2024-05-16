package ru.alex0d.investmentanalyst.config

import com.aallam.openai.api.http.Timeout
import com.aallam.openai.client.OpenAI
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import kotlin.time.Duration.Companion.seconds

@Configuration
class OpenAIConfig {

    @Bean
    fun openAIInstance(@Value("\${application.openai.key}") token: String): OpenAI {
        return OpenAI(
            token = token,
            timeout = Timeout(socket = 60.seconds),
        )
    }
}