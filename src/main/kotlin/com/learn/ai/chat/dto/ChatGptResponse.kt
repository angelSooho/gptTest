package com.learn.ai.chat.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class ChatGptResponse(
    val id: String,
    @JsonProperty("object")
    val obj: String,
    val created: String,
    val model: String,
    val choices: List<Choice>
) {

    data class Choice(
        val index: Int,
        val message: Message,
    )
}
