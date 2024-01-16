package com.learning.gpt.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.gpt.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatGptService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public GptModelsResponseDto models() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "https://api.openai.com/v1/models",
                String.class);

        GptModelsResponseDto gptModelsResponseDto = null;
        try {
            gptModelsResponseDto = objectMapper.readValue(response.getBody(), GptModelsResponseDto.class);
        } catch (JsonProcessingException e) {
            log.error("Error parsing response from OpenAI Server", e);
        }
        return gptModelsResponseDto;
    }

    public GptResponseDto chat(String model, String prompt, String endpointCharged) {
        List<Message> prompts = List.of(
                new Message("user", prompt));
        GptRequestDto request = new GptRequestDto(model, prompts, 1, 256, 1, 0, 0);

        // OpenAI server로 restTemplate을 통해 request를 보내고 response를 받는다.
        GptResponseDto gptResponse = restTemplate.postForObject(endpointCharged, request, GptResponseDto.class);
        if (gptResponse != null) {
            return gptResponse;
        } else {
            throw new RuntimeException("Error parsing response from OpenAI Server");
        }
    }
}
