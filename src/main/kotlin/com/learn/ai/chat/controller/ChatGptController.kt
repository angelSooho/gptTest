package com.learn.ai.chat.controller

import com.learn.ai.chat.dto.ChatGptResponse
import com.learn.ai.chat.dto.PromptRequest
import com.learn.ai.chat.dto.GptModelsResponse
import com.learn.ai.chat.service.ChatGptService
import org.jetbrains.annotations.NotNull
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/ai/gpt")
class ChatGptController(
    private val chatGptService: ChatGptService,
) {

    @GetMapping("/models")
    fun getModels(): ResponseEntity<GptModelsResponse> {
        return ResponseEntity.ok().body(chatGptService.getModels())
    }

    @GetMapping("/chat")
    fun chat(@NotNull @RequestParam("model") model: String,
             @NotNull @RequestBody prompt: PromptRequest): ResponseEntity<ChatGptResponse> {
        return ResponseEntity.ok().body(chatGptService.chat(model, prompt))
    }
}