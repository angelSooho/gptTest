package com.learning.gpt.chat;

import com.learning.gpt.chat.dto.GptModelsResponseDto;
import com.learning.gpt.chat.dto.GptResponseDto;
import com.learning.gpt.chat.service.ChatGptService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai/gpt")
@RequiredArgsConstructor
public class ChatGptController {

    @Value("${openai.endpoint.gpt-free}")
    private String endpointFree;

    @Value("${openai.endpoint.gpt-charged}")
    private String endpointCharged;

    private final ChatGptService chatGPTService;

    @GetMapping("/models")
    public ResponseEntity<GptModelsResponseDto> selectModelList() {
        return ResponseEntity.ok().body(chatGPTService.models());
    }

    @GetMapping("/chat")
    public ResponseEntity<GptResponseDto> chat(
            @NotNull @RequestParam("model") String model,
            @NotNull @RequestParam("prompt") String prompt){
        return ResponseEntity.ok().body(chatGPTService.chat(model, prompt, endpointCharged));
    }
}