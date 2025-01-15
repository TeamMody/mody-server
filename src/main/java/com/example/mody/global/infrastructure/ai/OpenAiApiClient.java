package com.example.mody.global.infrastructure.ai;

import com.example.mody.global.infrastructure.ai.dto.ChatGPTRequest;
import com.example.mody.global.infrastructure.ai.dto.ChatGPTResponse;
import com.example.mody.global.infrastructure.ai.dto.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

/**
 * WebClient를 사용하여 OpenAi Api와 통신. OpenAi Api와 통신하기 위한 필수적인 헤더들 추가하는 클래스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAiApiClient {

    private final WebClient webClient;

    public ChatGPTResponse sendRequestToModel(String model, List<Message> messages, int maxTokens, double temperature) {
        ChatGPTRequest request = new ChatGPTRequest(model, messages, maxTokens, temperature);
        log.info("request: {}", request);

        return webClient.post()
                .uri("/chat/completions")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ChatGPTResponse.class)
                .block(); // 동기식 처리
    }
}