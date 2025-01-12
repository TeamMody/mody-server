package com.example.mody.domain.bodytype.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 체형 분석 응답 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "체형 분석 결과 응답 DTO")
public class BodyTypeAnalysisResponse {

    @Schema(description = "사용자 이름", example = "장민수")
    private String name;

    @Schema(description = "체형 분석 결과")
    private BodyTypeAnalysis bodyTypeAnalysis;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "체형 분석 결과")
    public static class BodyTypeAnalysis {

        @Schema(description = "체형 유형", example = "네추럴")
        private String type;

        @Schema(description = "체형 설명", example = "장민수님의 체형은 큰 뼈대와 넓은 어깨, 굵은 쇄골, 그리고 허리의 굴곡이 뚜렷하지 않은 네추럴 유형에 해당합니다. 전체적으로 다부지고 탄탄한 신체구조를 가지고 있으며 다리와 엉덩이가 비교적 길고 입체적입니다.")
        private String description;

        @Schema(description = "스타일링 제안")
        private FeatureBasedSuggestions featureBasedSuggestions;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "스타일링 제안")
    public static class FeatureBasedSuggestions {

        @Schema(description = "강조할 부분", example = "장민수님은 넓고 탄탄한 어깨와 긴 다리를 활용한 스타일링이 가능합니다. 어깨와 다리의 비율을 강조하는 깔끔한 실루엣의 재킷과 팬츠는 체형의 균형감을 극대화시킵니다.")
        private String emphasize;

        @Schema(description = "보완할 부분", example = "허리 굴곡이 뚜렷하지 않으므로, 허리선을 살짝 강조하는 디자인이나 레이어드 스타일링으로 몸의 중간 라인을 정의하면 더욱 세련된 인상을 줄 수 있습니다.")
        private String enhance;
    }
}
