package com.learning.gpt.whisper.controller;

import com.learning.gpt.whisper.dto.WhisperResponseDto;
import com.learning.gpt.whisper.service.WhisperService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/whisper")
@RequiredArgsConstructor
public class WhisperController {

    @Value("${openai.endpoint.whisper}")
    private String whisperEndpoint;

    private final WhisperService whisperService;

    @PostMapping("send")
    public ResponseEntity<WhisperResponseDto> sendAudioFile(
            @NotNull @RequestParam("pageNumber") Integer pageNumber,
            @RequestParam("audioFile") MultipartFile audioFile) {
        return ResponseEntity.ok().body(
                whisperService.transcribeAudioFile(pageNumber, audioFile, whisperEndpoint)
        );
    }
}
