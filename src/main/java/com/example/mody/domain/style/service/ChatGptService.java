package com.example.mody.domain.style.service;

import com.example.mody.domain.style.dto.request.StyleRecommendRequest;
import com.example.mody.domain.style.dto.response.StyleRecommendResponse;
import com.example.mody.domain.style.templates.PromptManager;
import com.example.mody.global.common.exception.RestApiException;
import com.example.mody.global.common.exception.code.status.AnalysisErrorStatus;
import com.example.mody.global.infrastructure.ai.OpenAiApiClient;
import com.example.mody.global.infrastructure.ai.dto.request.ChatGptRequest;
import com.example.mody.global.infrastructure.ai.dto.response.ChatGptResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatGptService {

    private final OpenAiApiClient openAiApiClient;
    private final PromptManager promptManager;
    private final ObjectMapper objectMapper;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.max-tokens}")
    private int maxTokens;

    @Value("${openai.temperature}")
    private double temperature;

    private final String systemRole = "system";

    private final String userRole = "user";

    public StyleRecommendResponse recommendStyle(StyleRecommendRequest styleRecommendRequest){

        //사용자의 체형타입으로 받아오도록 수정할 예정입니다.
        String bodyType = """
                {
                "name": "영희",
                "bodyTypeAnalysis": {
                "type": "네추럴",
                "description": "영희님의 체형은 골격이 크고 힘줄이 돋보이며, 어깨와 허벅지가 발달한 특징을 가지고 있습니다. 넓은 어깨와 입체적인 엉덩이가 시각적으로 돋보이는 체형으로, 전체적으로 강인하고 조화로운 느낌을 줍니다.",
                "featureBasedSuggestions": {
                "emphasize": "쇄골과 어깨 라인을 드러내는 디자인을 추천합니다. 브이넥 상의나 오프숄더 스타일은 쇄골과 긴 목을 더욱 돋보이게 합니다. 또한, 허벅지와 긴 다리를 강조할 수 있는 스트레이트 핏 팬츠나 하이웨이스트 스커트를 추천합니다.",
                "enhance": "허리가 굴곡이 적은 편이므로, 허리 라인을 시각적으로 강조할 수 있는 스타일링이 좋습니다. 벨트나 허리 라인을 잡아주는 드레스, 상체에 볼륨을 주는 디테일이 도움이 됩니다."
                }
                }
                }
                """;

        //스타일 추천 프롬프트 생성
        String prompt = promptManager.createRecommendStylePrompt(bodyType, styleRecommendRequest);

        //openAiApiClient로 gpt 답변 생성
        ChatGptResponse response = openAiApiClient.sendRequestToModel(
                model,
                List.of(
                        new ChatGptRequest.ChatGptMessage(systemRole, prompt)
                ),
                maxTokens,
                temperature);

        String content = response.getChoices().get(0).getMessage().getContent().trim();

        try{
            return objectMapper.readValue(content, StyleRecommendResponse.class);
        } catch (JsonMappingException e) {
            throw new RestApiException(AnalysisErrorStatus._GPT_ERROR);
        } catch (JsonProcessingException e) {
            throw new RestApiException(AnalysisErrorStatus._GPT_ERROR);
        }
    }
}
