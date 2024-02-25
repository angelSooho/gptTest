package com.learn.ai.chat.dto

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank

data class SentenceFixResponse(
    @JsonProperty("original_word")
    val originalWord: String,
    @JsonProperty("spoken_word")
    val spokenWord: String,
    @JsonProperty("word_idx")
    val wordIdx: WordIdx,
    var fix: String // Different
) {
    data class WordIdx(
        val start: Int,
    )
}
