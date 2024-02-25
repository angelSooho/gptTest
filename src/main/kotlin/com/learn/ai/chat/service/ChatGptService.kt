package com.learn.ai.chat.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.learn.ai.chat.dto.*
import com.learn.ai.config.GptProperties
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class ChatGptService(
    val restTemplate: RestTemplate,
    val objectMapper: ObjectMapper,

    val gptProperties: GptProperties,
) {

    val log: Logger = LoggerFactory.getLogger(ChatGptService::class.java)

    fun getModels(): GptModelsResponse {
        val header = HttpHeaders()
        header.contentType = MediaType.APPLICATION_JSON
        header.setBearerAuth(gptProperties.apiKey)
        val entity = HttpEntity<String>(header)
        val response = restTemplate.exchange(
            gptProperties.url,
            HttpMethod.GET,
            entity,
            String::class.java
        )

        var gptModelsResponse: GptModelsResponse = GptModelsResponse("", emptyList())
        try{
            gptModelsResponse = objectMapper.readValue(response.body, GptModelsResponse::class.java)
        } catch (e: Exception) {
            log.info("Error: ${e.message}")
        }

        return gptModelsResponse
    }

    fun chat(model: String, prompt: PromptRequest): ChatGptResponse {
        val prompts = listOf(Message("user", prompt.spokenScript))
        val request = GptHttpRequest(
            model,
            prompts,
            1f, 256, 1, 0, 0
        )

        val header = HttpHeaders()
        header.contentType = MediaType.APPLICATION_JSON
        header.setBearerAuth(gptProperties.apiKey)
        val entity = HttpEntity<GptHttpRequest>(request, header)
        val response = restTemplate.exchange(
            gptProperties.endpoint.gptPaid,
            HttpMethod.POST,
            entity,
            String::class.java
        )

        var chatGptResponse: ChatGptResponse = ChatGptResponse("", "", "", "", emptyList())
        try{
            chatGptResponse = objectMapper.readValue(response.body, ChatGptResponse::class.java)
        } catch (e: Exception) {
            log.info("Error: ${e.message}")
        }

        return chatGptResponse
    }
}
