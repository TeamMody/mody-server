package com.example.mody.domain.fashionItem.controller;

import com.example.mody.domain.auth.security.CustomUserDetails;
import com.example.mody.domain.fashionItem.dto.request.FashionItemRequest;
import com.example.mody.domain.fashionItem.dto.response.ItemLikeResponse;
import com.example.mody.domain.fashionItem.dto.response.ItemRecommendResponse;
import com.example.mody.domain.fashionItem.dto.response.ItemsResponse;
import com.example.mody.domain.fashionItem.service.ItemCommandService;
import com.example.mody.domain.fashionItem.service.ItemQueryService;
import com.example.mody.global.common.base.BaseResponse;
import com.example.mody.global.dto.response.CursorResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
            ),
            @ApiResponse(
                    responseCode = "ITEM404",
                    description = "존재하지 않는 fashion item의 아이디를 입력했을 떄 발생합니다.",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
						{
							"timestamp": "2025-01-25T16:02:42.4014717",
							"code": "ITEM404",
							"message": "해당 패션 아이템을 찾을 수 없습니다."
						}
						"""
                            )
                    )
            )
    })
    public BaseResponse<ItemLikeResponse> toggleLike(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable("fashionItemId")
            @Parameter(
                    description = "좋아요를 누를 패션 아이템 ID",
                    required = true,
                    example = "1"
            )
            Long fashionItemId) {

        ItemLikeResponse response = itemCommandService.toggleLike(fashionItemId, customUserDetails.getMember().getId());
        return BaseResponse.onSuccess(response);
    }
}
