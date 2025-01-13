package com.example.mody.global.infrastructure.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 요청, 응답에서 사용하는 메시지
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private String role; // "system", "user", "assistant"
    private String content;
}