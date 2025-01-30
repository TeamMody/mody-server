package com.example.mody.domain.fashionItem.controller;

import com.example.mody.domain.auth.security.CustomUserDetails;
import com.example.mody.domain.fashionItem.dto.request.FashionItemRequest;
import com.example.mody.domain.fashionItem.dto.response.FashionItemRecommendResponse;
import com.example.mody.domain.fashionItem.service.FashionItemCommandService;
import com.example.mody.global.common.base.BaseResponse;
import com.example.mody.global.util.CustomAuthenticationEntryPoint;
import io.swagger.v3.oas.annotations.Operation;
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
public class FashionItemController {

    private final FashionItemCommandService fashionItemCommandService;

//    @Operation(summary = "패션 아이템 추천 결과 조회 API", description = "사용자 본인의 패션 아이템 추천 결과를 조회합니다.")
//    @GetMapping("/result")
//    @ApiResponses({
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "패션 아이템 추천 결과 조회 성공"
//            )
//    })
//    public BaseResponse<FashionItemRecommendResponse> getRecommendedFashionItem(
//            @AuthenticationPrincipal CustomUserDetails customUserDetails
//            ) {
//        return BaseResponse.onSuccess()
//    }

    @Operation(summary = "패션 아이템 추천 API", description = "OpenAI를 통해 패션 아이템 추천을 받는 API입니다.")
    @PostMapping("/result")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "패션 아이템 추천 성공"
                    //content =
            )
    })
    public BaseResponse<FashionItemRecommendResponse> recommendFashionItem(
            @Valid @RequestBody FashionItemRequest request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        FashionItemRecommendResponse response = fashionItemCommandService.recommendItem(
                request, customUserDetails.getMember());
        return BaseResponse.onSuccess(response);
    }

}
