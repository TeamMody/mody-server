package com.example.mody.global.infrastructure.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

/**
 * OpenAI 요청 DTO
 */
@Data
@AllArgsConstructor
public class ChatGPTRequest {
    private String model;
    private List<Message> messages;
    private int max_tokens;
    private double temperature;
}