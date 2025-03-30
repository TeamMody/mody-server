package com.example.mody.domain.recommendation.dto.response.analysis;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.List;

@Schema(description = "오늘 날씨에 어울리는 패션 추천 응답 정보")
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class WeatherStyleAnalysisResponse {

    @Schema(description = "추천 컨셉", example = "비 오는 날의 스트릿 & 힙 스타일")
    private String concept;

    @Schema(description = "이미지 URL",
            example = "https://i.pinimg.com/236x/32/87/f8/3287f86756200b3c8d9d28181aaddeae.jpg")
    private String imageUrl;

    @Schema(description = "스타일 방향 정보")
    private StyleDirection styleDirection;

    @Schema(description = "패션 팁 목록")
    private List<String> weatherTip;

    @Schema(description = "추천 스타일링 정보")
    private RecommendedStyling recommendedStyling;

    @Schema(description = "스타일 방향 정보")
    @Getter
    @Builder(toBuilder = true)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class StyleDirection {
        @Schema(description = "설명",
                example = "비가 오는 날에 적합하면서도 힙한 스트릿 스타일을 유지할 수 있는 패션을 제안합니다.")
        private String explanation;

        @Schema(description = "스타일 선호도",
                example = "스트레이트 체형을 최대한 돋보이게 하면서 개성을 살린 스타일링을 추천합니다.")
        private String stylePreference;

        @Schema(description = "날씨 적응",
                example = "방수 소재와 기능적인 디자인을 활용하여 비를 막고, 동시에 패션 감각도 유지할 수 있습니다.")
        private String weatherAdaptation;
    }

    @Schema(description = "추천 스타일링 정보")
    @Getter
    @Builder(toBuilder = true)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class RecommendedStyling {
        @Schema(description = "제목", example = "비 오는 날의 힙한 스트릿웨어")
        private String title;

        @Schema(description = "스타일 설명",
                example = "오버사이즈 방수 재킷과 편안한 핏의 팬츠를 매치하여 활동성과 방수 기능을 모두 갖춘 스타일을 연출하세요. 스니커즈나 방수 부츠와 함께하시면 실용성과 스타일 모두를 챙길 수 있습니다.")
        private String styleDescription;

        @Schema(description = "선택 이유",
                example = "이 스타일은 비 오는 날에도 활동적이면서도 자기만의 힙한 스타일을 표현할 수 있는 최적의 선택입니다. 스트레이트 체형은 오버사이즈 아이템과 잘 어울리며, 편안하게 스타일링할 수 있습니다.")
        private String reason;
    }

    public WeatherStyleAnalysisResponse from(String imageUrl) {
        return this.toBuilder()
                .imageUrl(imageUrl)
                .build();
    }
}
