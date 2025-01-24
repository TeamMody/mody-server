package com.example.mody.domain.style.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mody.domain.auth.security.CustomUserDetails;
import com.example.mody.domain.style.dto.request.StyleRecommendRequest;
import com.example.mody.domain.style.dto.response.CategoryResponse;
import com.example.mody.domain.style.dto.response.StyleRecommendResponse;
import com.example.mody.domain.style.service.StyleCommandService;
import com.example.mody.domain.style.service.StyleQueryService;
import com.example.mody.global.common.base.BaseResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "스타일 추천", description = "스타일 추천 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/style-analysis")
public class StyleController {

	private final StyleCommandService styleCommandService;
	private final StyleQueryService styleQueryService;

	@Operation(summary = "스타일 카테고리 조회 API", description = "스타일을 위한 스타일, 어필 카테고리를 반환하는 API")
	@GetMapping
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "스타일 카테고리 조회 성공",
			content = @Content(schema = @Schema(implementation = BaseResponse.class))
		)
	})
	public BaseResponse<CategoryResponse> getStyleCategories() {
		//서비스에서 데이터를 조회하여 반환
		CategoryResponse categoryResponse = styleQueryService.getCategories();
		return BaseResponse.onSuccess(categoryResponse);
	}

	@Operation(summary = "스타일 추천 결과 조회 API", description = "스타일 추천 결과를 조회합니다.")
	@GetMapping("/result")
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "스타일 추천 결과 조회 성공",
			content = @Content(schema = @Schema(implementation = StyleRecommendResponse.class))
		),
		@ApiResponse(
			responseCode = "400",
			description = "스타일 추천 결과를 찾을 수 없음",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					value = """
						{
						  "timestamp": "2025-01-17T00:48:53.9237864",
						  "code": "STYLE404",
						  "message": "스타일 추천 결과를 찾을 수 없습니다."
						}
						"""
				)
			)
		)
	})
	public BaseResponse<StyleRecommendResponse> getRecommendedStyle(
		@AuthenticationPrincipal CustomUserDetails customUserDetails
	) {
		return BaseResponse.onSuccess(styleQueryService.getRecommendedStyle(customUserDetails.getMember()));
	}

	@Operation(summary = "스타일 추천 결과 반환 API", description = "스타일 분석 후 그 결과를 반환합니다.")
	@PostMapping("/result")
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "스타일 추천 성공",
			content = @Content(schema = @Schema(implementation = StyleRecommendResponse.class))
		),
		@ApiResponse(
			responseCode = "400",
			description = "사용자의 체형 정보를 찾을 수 없음",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					value = """
						{
						  "timestamp": "2025-01-17T00:48:53.9237864",
						  "code": "MEMBER_BODY_TYPE404",
						  "message": "체형 분석 결과를 찾을 수 없습니다."
						}
						"""
				)
			)
		)
	})
	public BaseResponse<StyleRecommendResponse> recommendStyle(
		@Valid @RequestBody StyleRecommendRequest request,
		@AuthenticationPrincipal CustomUserDetails customUserDetails) {

		StyleRecommendResponse response = styleCommandService.recommendStyle(
			request, customUserDetails.getMember());
		return BaseResponse.onSuccess(response);
	}
}
