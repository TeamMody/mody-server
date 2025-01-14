package com.example.mody.global.templates;

import com.example.mody.domain.member.enums.Gender;
import org.springframework.stereotype.Component;

/**
 * PromptTemplate에 맞게 프롬프트를 생성, 관리하는 클래스
 */
@Component
public class PromptManager {

    // 체형 분석 프롬프트 생성 메서드
    public String createBodyTypeAnalysisPrompt(String nickName, Gender gender, String answers) {
        PromptTemplate template = new PromptTemplate();
        return template.fillTemplate(
                """
                ## 명령
                닉네임과 성별, 그리고 사용자의 답변을 기반으로 체형 타입(네추럴, 스트레이트, 웨이브 중 하나)을 분석하고, 설명과 스타일링 팁을 제공해줘.
                결과는 JSON 형식으로 반환해줘.

                ## 사용자 정보
                닉네임: %s
                성별: %s

                ## 답변
                %s
                """.formatted(nickName, gender, answers),
                """
                {
                  "name": "<사용자 닉네임>",
                  "bodyTypeAnalysis": {
                    "type": "<체형 유형>",
                    "description": "<체형 설명>",
                    "featureBasedSuggestions": {
                      "emphasize": "<강조할 부분>",
                      "enhance": "<보완할 부분>"
                    }
                  }
                }
                """
        );
    }
}
