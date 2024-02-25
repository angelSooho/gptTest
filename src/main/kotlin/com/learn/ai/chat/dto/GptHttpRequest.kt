package com.learn.ai.chat.dto

class GptHttpRequest(
    val model: String,
    val messages: List<Message>,
    val temperature: Float,
    val max_tokens: Int,
    val top_p: Int,
    val frequency_penalty: Int,
    val presence_penalty: Int,
)