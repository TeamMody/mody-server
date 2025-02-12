package com.example.mody.global.templates;

import com.example.mody.domain.member.enums.Gender;
import com.example.mody.domain.recommendation.dto.request.MemberInfoRequest;
import com.example.mody.domain.recommendation.dto.request.RecommendRequest;
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

    public String createRecommendStylePrompt(MemberInfoRequest memberInfoRequest, RecommendRequest recommendRequest) {
        PromptTemplate template = new PromptTemplate();
        String recommendRequestContent = createRecommendRequestContent("패션 스타일 추천해줘", memberInfoRequest, recommendRequest);
        return template.fillTemplate(
                recommendRequestContent,
                """
                        {
                            "nickName": string,
                            "recommendedStyle": string,
                            "introduction": string,
                            "styleDirection": string,
                            "practicalStylingTips": string,
                            "imageUrl": string
                            }
                        }
                        """
        );
    }

    public String createRecommendItemPrompt(MemberInfoRequest memberInfoRequest, RecommendRequest recommendRequest) {
        PromptTemplate template = new PromptTemplate();
        String recommendRequestContent = createRecommendRequestContent("패션 아이템 추천해줘", memberInfoRequest, recommendRequest);
        return template.fillTemplate(
                recommendRequestContent,
                """
                        {
                            "nickName": string,
                            "item": string,
                            "description": string,
                            "imageUrl": string,
                        }
                        """
                );
    }

    private String createRecommendRequestContent(String additionalText, MemberInfoRequest memberInfoRequest, RecommendRequest recommendRequest) {
        return """
                         ##명령
                        사용자의 체형 타입과 원하는 스타일, 선호하지 않는 스타일, 보여주고 싶은 이미지를 고려해 스타일을 추천해줘.
                        imageUrl은 웹에서 검색 후 존재하는 사진을 jpg 형식으로 가져와줘.
                                                
                        ## 사용자 정보
                        닉네임: %s
                        성별: %s
                                                
                        ## 사용자 체형 타입
                        \'%s\'
                        ## 사용자 체형 정보
                        \'%s\'
                                                
                        ## 사용자의 취향에 해당하는 스타일
                        \'%s\'
                        ## 사용자가 선호하지 않는 스타일
                        \'%s\'
                        ## 사용자가 보여주고 싶은 이미지
                        \'%s\'
                                                
                        %s
                        """
                        .formatted(memberInfoRequest.getNickName(),
                                memberInfoRequest.getGender(),
                                memberInfoRequest.getBodyTypeName(),
                                memberInfoRequest.getBody(),
                                recommendRequest.getPreferredStyles(),
                                recommendRequest.getDislikedStyles(),
                                recommendRequest.getAppealedImage(),
                                additionalText
                        );
    }

}
