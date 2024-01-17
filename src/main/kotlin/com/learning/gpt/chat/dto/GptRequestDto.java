package com.learning.gpt.chat.dto;

import java.util.List;

public record GptRequestDto(String model, List<Message> messages,
                            float temperature, int max_tokens, int top_p,
                            int frequency_penalty, int presence_penalty) {
}

/**
 *
 *     private String model;           // 모델명
 *     private List<Message> messages; // 질문들
 *     private float temperature;      // 답변 다양성
 *     private int max_tokens;         // 답변 최대 길이
 *     private int top_p;              // 답변 다양성
 *     private int frequency_penalty;  // 답변 중복 방지
 *     private int presence_penalty;   // 답변 중복 방지
 *
 * */
