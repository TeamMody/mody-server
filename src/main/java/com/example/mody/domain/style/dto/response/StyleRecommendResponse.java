package com.example.mody.domain.style.dto.response;

import com.example.mody.domain.style.entity.Style;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "스타일 추천 응답 DTO")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class StyleRecommendResponse {

	@Schema(
		description = "회원 이름",
		example = "영희"
	)
	private String name;
	private StyleRecommendation styleRecommendation;

	public static StyleRecommendResponse of(String name, StyleRecommendation styleRecommendation) {
		return StyleRecommendResponse.builder()
			.name(name)
			.styleRecommendation(styleRecommendation)
			.build();
	}

	@Schema(description = "스타일 추천 정보")
	@Getter
	@Builder
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@AllArgsConstructor
	public static class StyleRecommendation {

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
			example = "https://example.com/street-vintage-style.jpg"
		)
		private String imageUrl;

		public static StyleRecommendation from(Style style) {
			return StyleRecommendation.builder()
				.recommendedStyle(style.getRecommendedStyle())
				.introduction(style.getIntroduction())
				.styleDirection(style.getStyleDirection())
				.practicalStylingTips(style.getPracticalStylingTips())
				.imageUrl(style.getStyleImage().getImageUrl())
				.build();
		}

	}
}
