package com.example.mody.domain.style.controller;

import com.example.mody.domain.fashionItem.dto.response.ItemLikeResponse;
import com.example.mody.domain.style.dto.response.StyleRecommendResponses;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
			responseCode = "STYLE404",
			description = "유저가 추천받은 스타일이 존재하지 않음",
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
	public BaseResponse<StyleRecommendResponses> getRecommendedStyle(
		@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@RequestParam(name = "cursor", required = false) Long cursor,
		@RequestParam(name = "size", defaultValue = "15") Integer size
	) {
		return BaseResponse.onSuccess(styleQueryService.getRecommendedStyle(customUserDetails.getMember(), cursor, size));
	}

	@Operation(summary = "스타일 추천 API", description = "스타일 분석 후 그 결과를 반환합니다.")
	@PostMapping("/result")
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "스타일 추천 성공",
			content = @Content(schema = @Schema(implementation = StyleRecommendResponse.class))
		),
		@ApiResponse(
			responseCode = "MEMBER_BODY_TYPE404",
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
		),
		@ApiResponse(
			responseCode = "COMMON402",
				description = "카테고리 리스트가 비어있을 때 발생합니다. " +
					"선호하는 스타일/ 선호하지 않는 스타일/ 보여주고 싶은 이미지 목록으로 표시됩니다.",
				content = @Content(
					mediaType = "application/json",
					examples = @ExampleObject(
						value = """
							{
								"timestamp": "2025-01-25T15:57:08.7901651",
								"code": "COMMON402",
								"message": "Validation Error입니다.",
								"result": {
									"preferredStyles": "선호하는 스타일 목록은 비어 있을 수 없습니다."
								}
							}
							"""
						)
				)
		),
		@ApiResponse(
			responseCode = "ANALYSIS108",
			description = "GPT 응답 형식이 적절하지 않을 때 발생합니다.",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					value = """
						{
							"timestamp": "2025-01-25T16:02:42.4014717",
							"code": "ANALYSIS108",
							"message": "GPT가 올바르지 않은 답변을 했습니다. 관리자에게 문의하세요."
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

	@Operation(summary = "스타일 추천 좋아요 API", description = "스타일 추천에 대한 좋아요 기능")
	@PostMapping("/{styleId}/like")
	@ApiResponses({
			@ApiResponse(responseCode = "COMMON200", description = "스타일 추천에 좋아요 성공"),
			@ApiResponse(responseCode = "STYLE404", description = "요청한 스타일 추천 결과물이 존재하지 않는 경우")
	})
	public BaseResponse<ItemLikeResponse> toggleStyleLike(
			@PathVariable(name = "styleId") Long styleId,
			@AuthenticationPrincipal CustomUserDetails customUserDetails) {
		ItemLikeResponse response = styleCommandService.toggleLike(styleId, customUserDetails.getMember());
		return BaseResponse.onSuccess(null);
	}
}
