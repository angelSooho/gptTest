package com.learning.gpt.controller;

import com.learning.gpt.dto.*;
import com.learning.gpt.service.ChatGptService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ai/gpt")
@RequiredArgsConstructor
public class ChatGptController {

    @Value("${gpt.endpoint.gpt-free}")
    private String endpointFree;

    @Value("${gpt.endpoint.gpt-charged}")
    private String endpointCharged;

    private final ChatGptService chatGPTService;

    @GetMapping("/chat")
    public ResponseEntity<GptResponseDto> chat(
            @NotNull @RequestParam("model") String model,
            @NotNull @RequestParam("prompt") String prompt){
        return ResponseEntity.ok().body(chatGPTService.chat(model, prompt, endpointCharged));
    }

    @GetMapping("/models")
    public ResponseEntity<GptModelsResponseDto> selectModelList() {
        return ResponseEntity.ok().body(chatGPTService.models());
    }
}