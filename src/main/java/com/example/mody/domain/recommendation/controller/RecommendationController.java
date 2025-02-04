package com.example.mody.domain.recommendation.controller;

import com.example.mody.domain.auth.security.CustomUserDetails;
import com.example.mody.domain.recommendation.dto.request.RecommendRequest;
import com.example.mody.domain.recommendation.dto.response.CategoryResponse;
import com.example.mody.domain.recommendation.dto.response.RecommendLikeResponse;
import com.example.mody.domain.recommendation.dto.response.RecommendResponse;
import com.example.mody.domain.recommendation.dto.response.RecommendResponses;
import com.example.mody.domain.recommendation.service.RecommendationCommendService;
import com.example.mody.domain.recommendation.service.RecommendationQueryService;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "추천", description = "추천 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/recommendations")
public class RecommendationController {

    private final RecommendationQueryService recommendationQueryService;
    private final RecommendationCommendService recommendationCommendService;

    @Operation(summary = "추천 카테고리 조회 API", description = "추천을 위한 스타일, 어필 카테고리를 반환하는 API")
    @GetMapping("/categories")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "스타일 카테고리 조회 성공",
                    content = @Content(schema = @Schema(implementation = CategoryResponse.class))
            )
    })
    public BaseResponse<CategoryResponse> getStyleCategories() {
        //서비스에서 데이터를 조회하여 반환
        CategoryResponse categoryResponse = recommendationQueryService.getCategories();
        return BaseResponse.onSuccess(categoryResponse);
    }


    @Operation(summary = "스타일 추천 좋아요 API", description = "스타일 추천에 대한 좋아요 기능")
    @PostMapping("/{recommendationId}/like")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "스타일 추천에 좋아요 성공"),
            @ApiResponse(responseCode = "RECOMMENDATION404", description = "요청한 스타일 추천 결과물이 존재하지 않는 경우")
    })
    public BaseResponse<RecommendLikeResponse> toggleStyleLike(
            @PathVariable(name = "recommendationId") Long recommendationId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        RecommendLikeResponse response = recommendationCommendService.toggleLike(recommendationId, customUserDetails.getMember());
        return BaseResponse.onSuccess(response);
    }


    @Operation(summary = "추천 결과 조회 API", description = "사용자가 추천받은 결과를 마이페이지에서 조회하는 api입니다.")
    @GetMapping
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "스타일 추천 성공",
                    content = @Content(schema = @Schema(implementation = RecommendResponses.class))
            ),
            @ApiResponse(
                    responseCode = "RECOMMENDATION404",
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
    public BaseResponse<RecommendResponses> getRecommendations(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(name = "cursor", required = false) Long cursor,
            @RequestParam(name = "size", defaultValue = "15") Integer size
    ) {
        RecommendResponses responses = recommendationQueryService.getRecommendations(customUserDetails.getMember(), size, cursor);
        return BaseResponse.onSuccess(responses);
    }

    @Operation(summary = "스타일 추천 API", description = "스타일 분석 후 그 결과를 반환합니다.")
    @PostMapping("/style-analysis")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "스타일 추천 성공",
                    content = @Content(schema = @Schema(implementation = RecommendResponse.class))
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
    public BaseResponse<RecommendResponse> recommendStyle(
            @Valid @RequestBody RecommendRequest request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        RecommendResponse response = recommendationCommendService.recommendStyle(
                customUserDetails.getMember(), request);
        return BaseResponse.onSuccess(response);
    }

    @Operation(summary = "패션 아이템 추천 API", description = "패션아이템 분석 후 그 결과를 반환합니다.")
    @PostMapping("/fashion-item-analysis")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "패션 아이템 추천 성공",
                    content = @Content(schema = @Schema(implementation = RecommendResponse.class))
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
    public BaseResponse<RecommendResponse> recommendFashionItem(
            @Valid @RequestBody RecommendRequest request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        RecommendResponse response = recommendationCommendService.recommendFashionItem(
                customUserDetails.getMember(), request);
        return BaseResponse.onSuccess(response);
    }
}
