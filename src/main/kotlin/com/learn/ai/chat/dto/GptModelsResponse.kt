package com.learn.ai.chat.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class GptModelsResponse(
    @JsonProperty("object")
    val obj: String,
    val data: List<ModelData>,
) {
    data class ModelData(
        val id: String,
        @JsonProperty("object")
        val obj: String,
        val created: Long,
        @JsonProperty("owned_by")
        val ownedBy: String,
    )
}