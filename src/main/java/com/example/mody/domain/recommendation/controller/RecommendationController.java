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
public class RecommendationController implements RecommendationControllerInterface {

    private final RecommendationQueryService recommendationQueryService;
    private final RecommendationCommendService recommendationCommendService;


    // todo : 피그마에 있는 카테고리 순서대로 주기
    // 추천 카테고리 조회
    @GetMapping("/categories")
    public BaseResponse<CategoryResponse> getStyleCategories() {
        //서비스에서 데이터를 조회하여 반환
        CategoryResponse categoryResponse = recommendationQueryService.getCategories();
        return BaseResponse.onSuccess(categoryResponse);
    }

    // 추천 좋아요
    @PostMapping("/{recommendationId}/like")
    public BaseResponse<RecommendLikeResponse> toggleStyleLike(
            @PathVariable(name = "recommendationId") Long recommendationId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        RecommendLikeResponse response = recommendationCommendService.toggleLike(recommendationId, customUserDetails.getMember());
        return BaseResponse.onSuccess(response);
    }

    // 추천 결과 조회
    @GetMapping
    public BaseResponse<RecommendResponses> getRecommendations(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(name = "cursor", required = false) Long cursor,
            @RequestParam(name = "size", defaultValue = "15") Integer size
    ) {
        RecommendResponses responses = recommendationQueryService.getRecommendations(customUserDetails.getMember(), size, cursor);
        return BaseResponse.onSuccess(responses);
    }

    @PostMapping("/style-analysis")
    // 스타일 추천 API
    public BaseResponse<RecommendResponse> recommendStyle(
            @Valid @RequestBody RecommendRequest request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        RecommendResponse response = recommendationCommendService.recommendStyle(
                customUserDetails.getMember(), request);
        return BaseResponse.onSuccess(response);
    }

    // 스타일 아이템 추천 API
    @PostMapping("/fashion-item-analysis")
    public BaseResponse<RecommendResponse> recommendFashionItem(
            @Valid @RequestBody RecommendRequest request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        RecommendResponse response = recommendationCommendService.recommendFashionItem(
                customUserDetails.getMember(), request);
        return BaseResponse.onSuccess(response);
    }
}
