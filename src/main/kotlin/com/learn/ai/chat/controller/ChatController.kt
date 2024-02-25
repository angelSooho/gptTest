package com.learn.ai.chat.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.learn.ai.chat.dto.PromptRequest
import com.learn.ai.chat.dto.SentenceFixResponse
import com.learn.ai.config.GptProperties
import org.jetbrains.annotations.NotNull
import org.slf4j.Logger
import org.springframework.ai.chat.ChatResponse
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.ai.openai.OpenAiChatClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
@RequestMapping("/ai")
class ChatController(
    private val chatClient: OpenAiChatClient,
    private val objectMapper: ObjectMapper,
    private val gptProperties: GptProperties,
) {

    val log: Logger = org.slf4j.LoggerFactory.getLogger(this::class.java)

    @GetMapping("/generate")
    fun generate(@NotNull @RequestBody scripts: PromptRequest): List<SentenceFixResponse> {
        var prompt = gptProperties.feedbackPrompt
        prompt = prompt
            .replace("{original_script}", scripts.originalScript)
            .replace("{spoken_script}", scripts.spokenScript)
        var result = chatClient.call(prompt)
        println(result)
        result = result.replace("```json", "")
            .replace("```", "").trim()

        val data: List<SentenceFixResponse> = objectMapper.readValue(result)
        return data
    }

    @GetMapping("/generateStream")
    fun generateStream(@NotNull @RequestBody scripts: PromptRequest): Flux<ChatResponse> {
        val prompts = gptProperties.feedbackPrompt
        prompts.replace("{format_instructions}", objectMapper.writeValueAsString(SentenceFixResponse::class.java))
        prompts.replace("{original_script}", scripts.originalScript)
        prompts.replace("{spoken_script}", scripts.spokenScript)
        val prompt = Prompt(prompts)
        return chatClient.stream(prompt)
    }

    private fun findWordIndexes(originalScript: String, incorrectWords: List<String>): List<Triple<String, Int, Int>> {
        return incorrectWords.mapNotNull { word ->
            originalScript.indexOf(word)?.let { startIndex ->
                if (startIndex >= 0) {
                    Triple(word, startIndex, startIndex + word.length - 1)
                } else {
                    null
                }
            }
        }
    }
}