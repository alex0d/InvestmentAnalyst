package ru.alex0d.investmentanalyst.controller

import com.aallam.openai.api.chat.ChatCompletion
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import kotlinx.coroutines.runBlocking
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.alex0d.investmentanalyst.dto.TarotCard
import ru.alex0d.investmentanalyst.dto.TarotPredictionResponse
import ru.alex0d.investmentanalyst.dto.toRussianName

@RestController
@CrossOrigin
@RequestMapping("/api/tarot")
class TarotController(
    private val openAI: OpenAI
) {

    @Operation(summary = "Get tarot prediction", description = "Get tarot prediction by stock name")
    @ApiResponse(responseCode = "200", description = "Prediction retrieved successfully")
    @GetMapping("/{stockName}")
    fun getPrediction(@Parameter(description = "Stock name") @PathVariable stockName: String): ResponseEntity<TarotPredictionResponse> {
        val card = TarotCard.entries.random()  // secret esoteric technology

        val chatCompletionRequest = ChatCompletionRequest(
            model = ModelId("gpt-3.5-turbo"),
            messages = listOf(
                ChatMessage(
                    role = ChatRole.User,
                    content = "Тебе необходимо провести анализ акции по карте Таро. Твой ответ будет использован в соответствующем сервисе и не должен выглядеть как ответ чат-бота в диалоге. Ответ не должен содержать заголовков и различную предупреждающую информацию о необходимости провести другой анализ. При анализе наибольшее внимание удели котировкам. Придумай 3-4 абзаца. Карта: ${card.toRussianName()}"
                )
            )
        )

        val completion: ChatCompletion = runBlocking { openAI.chatCompletion(chatCompletionRequest) }
        completion.choices.first().message.content?.let {
            return ResponseEntity.ok(
                TarotPredictionResponse(
                    cardName = card.name.lowercase(),
                    prediction = it
                )
            )
        } ?: return ResponseEntity.badRequest().build()
    }
}