package com.example.mody.domain.chatgpt.service;

import com.example.mody.domain.bodytype.dto.BodyTypeAnalysisResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import com.example.mody.global.common.exception.RestApiException;
import com.example.mody.global.common.exception.code.status.AnalysisErrorStatus;
import com.example.mody.global.infrastructure.ai.OpenAiApiClient;
import com.example.mody.global.infrastructure.ai.dto.ChatGPTResponse;
import com.example.mody.global.infrastructure.ai.dto.Message;
import com.example.mody.global.templates.PromptManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * PromptManager를 통해 프롬프트를 생성하고, OpenAIApiClient를 사용하여 ChatGPT 모델에 요청을 보내 응답을 처리
 */
@Slf4j
@Service
@RequiredArgsConstructor
public final class ChatGptService {

    private final OpenAiApiClient openAiApiClient; // ChatGPT API와의 통신을 담당
    private final PromptManager promptManager; // 프롬프트 생성
    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 응답 변환

    @Value("${openai.model}")
    private String model; // OpenAI 모델

    @Value("${openai.max-tokens}")
    private int maxTokens; // 최대 토큰 수

    @Value("${openai.temperature}")
    private double temperature; // 생성된 응답의 창의성 정도

    private final String systemRole = "system"; // 대화에서의 역할(시스템 메시지)

    private final String userRole = "user"; // 대화에서의 역할(사용자 메시지)

    // 체형 분석 메서드
    public BodyTypeAnalysisResponse analyzeBodyType(String name, String gender, String answers) {

        // 템플릿 생성
        String prompt = promptManager.createBodyTypeAnalysisPrompt(name, gender, answers);

        // OpenAI API 호출
        ChatGPTResponse response = openAiApiClient.sendRequestToModel(
                model,
                List.of(
                        new Message(systemRole, prompt)
                ),
                maxTokens,
                temperature
        );

        // response에서 content 추출
        String content = response.getChoices().get(0).getMessage().getContent().trim();
        log.info("content: {}", content);

        // 백틱과 "json" 등을 제거
        if (content.startsWith("```")) {
            content = content.replaceAll("```[a-z]*", "").trim();
        }
        log.info("백틱 제거 후 content: {}", content);

        try {
            // content -> BodyTypeAnalysisResponse 객체로 변환
            return objectMapper.readValue(content, BodyTypeAnalysisResponse.class);
        } catch (JsonMappingException e) {
            throw new RestApiException(AnalysisErrorStatus._GPT_ERROR);
        } catch (JsonProcessingException e) {
            throw new RestApiException(AnalysisErrorStatus._GPT_ERROR);
        }
    }
}