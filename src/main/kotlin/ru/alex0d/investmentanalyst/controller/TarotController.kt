package ru.alex0d.investmentanalyst.controller

import io.grpc.Metadata
import io.grpc.stub.MetadataUtils
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.alex0d.investmentanalyst.dto.TarotCard
import ru.alex0d.investmentanalyst.dto.TarotPredictionResponse
import ru.alex0d.investmentanalyst.dto.toRussianName
import yandex.cloud.api.ai.foundation_models.v1.TextGenerationServiceGrpc
import yandex.cloud.api.ai.foundation_models.v1.completionRequest
import yandex.cloud.api.ai.foundation_models.v1.message

@RestController
@CrossOrigin
@RequestMapping("/api/tarot")
class TarotController(
    @Value("\${application.yandex.folder-id}")
    private val yandexFolderId: String
) {

    @GrpcClient("llm.api.cloud.yandex.net/foundationModels/v1/completion")
    private lateinit var yandexGptStub: TextGenerationServiceGrpc.TextGenerationServiceBlockingStub

    private val metadata = Metadata().apply {
        val key = Metadata.Key.of("x-folder-id", Metadata.ASCII_STRING_MARSHALLER)
        put(key, yandexFolderId)
    }

    @Operation(summary = "Get tarot prediction", description = "Get tarot prediction by stock name")
    @ApiResponse(responseCode = "200", description = "Prediction retrieved successfully")
    @GetMapping("/{stockName}")
    fun getPrediction(@Parameter(description = "Stock name") @PathVariable stockName: String): ResponseEntity<TarotPredictionResponse> {
        val card = TarotCard.entries.random()  // secret esoteric technology

        yandexGptStub
            .withInterceptors(MetadataUtils.newAttachHeadersInterceptor(metadata))
            .completion(completionRequest {
                modelUri = "gpt://$yandexFolderId/yandexgpt-lite/latest"
                messages += message {
                    role = "user"
                    text =
                        "Тебе необходимо проводить анализ акции по карте Таро. Твой ответ будет использован в соответствующем сервисе и не должен выглядеть как ответ чат-бота в диалоге. Ответ не должен содержать заголовков (в том число заголовок о том, по какой карте проводится анализ) и различную предупреждающую информацию о необходимости провести другой анализ. При анализе наибольшее внимание удели котировкам. Отвечай в эзотерическом стиле коротко, без воды, но чтобы было убедительно и живо. Придумывай по 3-4 абзаца. Формат ответа: простой текст. Проведи анализ акции по карте ${card.toRussianName()}."
                }
            }).let { completionResponse ->
                return ResponseEntity.ok(
                    TarotPredictionResponse(
                        cardName = card.name.lowercase(),
                        prediction = completionResponse.next().alternativesList.first().message.text
                    )
                )
            }
    }
}