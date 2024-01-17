package com.learning.gpt.chat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.gpt.chat.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
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
        String url = "https://api.openai.com/v1/models";

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class);

        GptModelsResponseDto gptModelsResponseDto = null;
        try {
            gptModelsResponseDto = objectMapper.readValue(response.getBody(), GptModelsResponseDto.class);
        } catch (JsonProcessingException e) {
            log.error("Error parsing response from OpenAI Server", e);
        }
        return gptModelsResponseDto;
    }

    public GptResponseDto chat(String model, String prompt, String endpointCharged) {
        List<Message> prompts = List.of(new Message("user", prompt));
        GptRequestDto request = new GptRequestDto(model, prompts, 1, 256, 1, 0, 0);

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<GptRequestDto> entity = new HttpEntity<>(request, headers);

        // OpenAI server로 restTemplate을 통해 request를 보내고 response를 받는다.
        GptResponseDto gptResponse = restTemplate.exchange(
                endpointCharged, HttpMethod.POST, entity, GptResponseDto.class).getBody();
        if (gptResponse != null) {
            return gptResponse;
        } else {
            throw new RuntimeException("Error parsing response from OpenAI Server");
        }
    }
}
