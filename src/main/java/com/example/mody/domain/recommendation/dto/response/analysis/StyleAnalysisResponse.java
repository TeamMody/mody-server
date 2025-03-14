package com.example.mody.domain.recommendation.dto.response.analysis;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "스타일 추천 정보")
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class StyleAnalysisResponse {

    private String nickName;

    @Schema(
            description = "추천하는 스타일",
            example = "네추럴 스트릿 빈티지"
    )
    public String recommendedStyle;

    @Schema(
            description = "스타일 추천 배경",
            example = "영희님의 체형은 네추럴 타입으로, 강인하고 조화로운 느낌을 주기 때문에... "
    )
    private String introduction;

    @Schema(
            description = "스타일 조언",
            example = "쇄골과 어깨 라인을 드러내는 브이넥 탑이나 오프숄더 블라우스와 함께..."
    )
    private String styleDirection;

    @Schema(
            description = "실질적인 스타일 팁",
            example = "벨트로 허리 라인을 강조하고, 볼륨감 있는 아우터를 활용하여..."
    )
    private String practicalStylingTips;

    @Schema(
            description = "이미지 url",
            example = "https://i.pinimg.com/236x/ad/c4/4f/adc44f56293a18fdb15f8b5fa5067dab.jpg"
    )
    private String imageUrl;

    public StyleAnalysisResponse from(String imageUrl) {
        return this.toBuilder()
                .imageUrl(imageUrl)
                .build();
    }
}
