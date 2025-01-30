package com.example.mody.domain.fashionItem.controller;

import com.example.mody.domain.auth.security.CustomUserDetails;
import com.example.mody.domain.fashionItem.dto.request.FashionItemRequest;
import com.example.mody.domain.fashionItem.dto.response.ItemRecommendResponse;
import com.example.mody.domain.fashionItem.dto.response.ItemsResponse;
import com.example.mody.domain.fashionItem.service.ItemCommandService;
import com.example.mody.domain.fashionItem.service.ItemQueryService;
import com.example.mody.global.common.base.BaseResponse;
import com.example.mody.global.dto.response.CursorResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "패션 아이템 추천", description = "패션 아이템 추천 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/fashion-item-analysis")
public class ItemController {

    private final ItemCommandService itemCommandService;
    private final ItemQueryService itemQueryService;

    @Operation(summary = "패션 아이템 추천 API", description = "OpenAI를 통해 패션 아이템 추천을 받는 API입니다.")
    @PostMapping("/result")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "패션 아이템 추천 성공"
                    //content =
            )
    })
    public BaseResponse<ItemRecommendResponse> recommendFashionItem(
            @Valid @RequestBody FashionItemRequest request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        ItemRecommendResponse response = itemCommandService.recommendItem(
                request, customUserDetails.getMember());
        return BaseResponse.onSuccess(response);
    }

    @Operation(summary = "패션 아이템 추천 결과 조회 API", description = "사용자 본인의 패션 아이템 추천 결과를 조회합니다.")
    @GetMapping("/result")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "패션 아이템 추천 결과 조회 성공"
            )
    })
    public BaseResponse<CursorResult<ItemsResponse>> getRecommendedFashionItem(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(name = "cursor", required = false) Long cursor,
            @RequestParam(name = "size", defaultValue = "15") Integer size

    ) {
        CursorResult<ItemsResponse> response = itemQueryService.getRecommendedItems(customUserDetails.getMember(), cursor, size);
        return BaseResponse.onSuccess(response);
    }

    @Operation(summary = "패션 아이템 좋아요 API", description = "패션 아이템 추천 결과에 좋아요를 누르거나 삭제합니다.")
    @PostMapping("/{fashionItemId}/like")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "좋아요 기능 성공"
            )
    })
    public BaseResponse<Void> toggleLike(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable("fashionItemId")
            @Parameter(
                    description = "좋아요를 누를 패션 아이템 ID",
                    required = true,
                    example = "1"
            )
            Long fashionItemId) {

        itemCommandService.toggleLike(fashionItemId, customUserDetails.getMember().getId());
        return BaseResponse.onSuccess(null);
    }
}
