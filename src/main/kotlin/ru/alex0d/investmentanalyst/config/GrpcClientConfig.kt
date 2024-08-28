package ru.alex0d.investmentanalyst.config

import io.grpc.CallCredentials
import net.devh.boot.grpc.client.security.CallCredentialsHelper
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GrpcClientConfig {

    @Bean
    fun yandexApiCredentials(@Value("\${application.yandex.key}") yandexApiKey: String): CallCredentials {
        return CallCredentialsHelper.authorizationHeader("Api-Key $yandexApiKey")
    }
}
