package com.example.mody.domain.recommendation.dto.response.analysis;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "특정 상황에 어울리는 패션 추천 응답 정보")
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OccasionStyleAnalysisResponse {

    @Schema(
            description = "추천 컨셉",
            example = "개강 후 첫 강의에서 자연스럽게 시선을 끌 수 있는 빈티지 캐주얼 무드"
    )
    private String concept;

    @Schema(
            description = "이미지 URL",
            example = "https://i.pinimg.com/236x/32/87/f8/3287f86756200b3c8d9d28181aaddeae.jpg"
    )
    private String imageUrl;

    @Schema(
            description = "요약",
            example = "웨이브 체형은 어깨보다 골반이 넓고 허리 라인이 잘록한 체형으로, 상체에 포인트를 주거나 허리선을 강조한 스타일이 잘 어울립니다. 빈티지 스타일은 부드러운 톤, 잔잔한 패턴, 고전적인 실루엣이 특징이며, 첫 강의에서 부담 없이 자신만의 분위기를 표현하기에 적합한 스타일입니다."
    )
    private String summary;

    @Schema(description = "추천 스타일링 정보")
    private RecommendedStyling recommendedStyling;

    @Schema(description = "추천 스타일링 정보")
    @Getter
    @Builder(toBuilder = true)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class RecommendedStyling {

        @Schema(
                description = "제목",
                example = "빈티지 캐주얼 무드"
        )
        private String title;

        @Schema(
                description = "코멘트",
                example = "빈티지한 톤과 실루엣은 꾸민 듯 안 꾸민 듯한 느낌을 주며, 웨이브 체형의 장점인 허리 라인을 살리면서도 첫 인상에서 부드러운 감성을 전달할 수 있어요."
        )
        private String comment;

        @Schema(
                description = "선택 이유",
                example = "크롭 상의와 하이웨이스트 하의의 조합은 웨이브 체형의 비율을 이상적으로 연출해주며, 빈티지 스타일은 유행에 민감하지 않으면서도 본인의 감각을 드러낼 수 있어 개강 첫 날 스타일로 탁월한 선택입니다."
        )
        private String reason;
    }

    public OccasionStyleAnalysisResponse from(String imageUrl) {
        return this.toBuilder()
                .imageUrl(imageUrl)
                .build();
    }
}
