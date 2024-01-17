package com.learning.gpt.chat.dto;

import java.util.List;

public record GptResponseDto(String completion, String created, String id, String model,
                             String object, List<Choice> choices) {
}
