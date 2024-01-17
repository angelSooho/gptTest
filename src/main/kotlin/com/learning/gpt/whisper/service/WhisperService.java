package com.learning.gpt.whisper.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.gpt.aop.ExeTimer;
import com.learning.gpt.whisper.dto.WhisperResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class WhisperService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @ExeTimer
    public WhisperResponseDto transcribeAudioFile(int pageNumber, MultipartFile audioFile, String endpoint) {
        // 파일을 임시 디렉토리에 저장
        File tempFile;
        try {
            tempFile = File.createTempFile("audio", ".mp3");
            audioFile.transferTo(tempFile);
        } catch (IOException e) {
            throw new RuntimeException("file saved fail", e);
        }

        // 멀티파트 요청 본문 구성
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        Resource resource = new FileSystemResource(tempFile);
        body.add("file", resource);
        body.add("model", "whisper-1");

        // HTTP 헤더 설정 (멀티파트 요청이므로 컨텐트 타입을 MULTIPART_FORM_DATA로 설정)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // OpenAI API 요청
        WhisperResponseDto whisperResponseDto;
        try {
            ResponseEntity<String> response;
            response = generateTextResponse(endpoint, requestEntity);
            whisperResponseDto = objectMapper.readValue(response.getBody(), WhisperResponseDto.class);
        } catch (Exception e) {
            tempFile.delete();
            log.error(e.getMessage());
            throw new RuntimeException("openai api request fail", e);
        }

        // 임시 파일 삭제
        tempFile.delete();
        return whisperResponseDto;
    }

    private ResponseEntity<String> generateTextResponse(String endpoint, HttpEntity<MultiValueMap<String, Object>> requestEntity) {
        return restTemplate.postForEntity(endpoint, requestEntity, String.class);
    }
}
