package com.example.mody.domain.style.templates;

import com.example.mody.domain.style.dto.request.StyleRecommendRequest;
import org.springframework.stereotype.Component;

@Component
public class PromptManager {

    //스타일 추천을 위한 프롬프트 생성
    public String createRecommendStylePrompt(String bodyType, StyleRecommendRequest styleRecommendRequest) {
        PromptTemplate template = new PromptTemplate();
        return template.fillTemplate(
                """
                        ##명령
                        사용자의 체형 타입과 원하는 스타일, 선호하지 않는 스타일, 보여주고 싶은 이미지를 고려해 스타일을 추천해줘.
                        imageUrl은 웹에서 검색 후 존재하는 사진을 jpg 형식으로 가져와줘.
                  
                        ## 사용자의 체형 타입
                        \'%s\'
                        ## 사용자의 취향에 해당하는 스타일
                        \'%s\'
                        ## 사용자가 선호하지 않는 스타일
                        \'%s\'
                        ## 사용자가 보여주고 싶은 이미지
                        \'%s\'
                        
                        패션 스타일 추천해줘
                        """
                        .formatted(bodyType, styleRecommendRequest.getPreferredStyles(), styleRecommendRequest.getDislikedStyles(), styleRecommendRequest.getAppealedImage())
                ,
                """
                        {
                            "name": <사용자 이름>,
                            "styleRecommendation": {
                                "recommendedStyle": <추천하는 스타일>,
                                "introduction": <스타일 추천 배경>,
                                "styleDirection": <스타일 조언>,
                                "practicalStylingTips": <실질적인 스타일 팁>,
                                "imageUrl": <이미지 url>
                            }
                        }
                        """
        );
    }
}
