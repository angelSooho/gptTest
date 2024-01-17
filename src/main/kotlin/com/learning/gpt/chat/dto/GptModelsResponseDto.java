package com.learning.gpt.chat.dto;

import java.util.List;

public record GptModelsResponseDto(String object, List<ModelData> data) {
}
