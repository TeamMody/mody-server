package com.example.mody.domain.bodytype.controller;

import com.example.mody.domain.auth.security.CustomUserDetails;
import com.example.mody.domain.bodytype.dto.request.BodyTypeAnalysisRequest;
import com.example.mody.domain.bodytype.dto.response.BodyTypeAnalysisResponse;
import com.example.mody.domain.bodytype.service.memberbodytype.MemberBodyTypeCommandService;
import com.example.mody.domain.bodytype.service.memberbodytype.MemberBodyTypeQueryService;
import com.example.mody.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "체형 분석", description = "체형 분석 API")
@RestController
@RequestMapping("/body-analysis")
@RequiredArgsConstructor
public class BodyTypeController {

    private final MemberBodyTypeCommandService memberBodyTypeCommandService;
    private final MemberBodyTypeQueryService memberBodyTypeQueryService;

    @PostMapping("/result")
    @Operation(summary = "체형 분석 API", description = "OpenAi를 사용해서 사용자의 체형을 분석하는 API입니다. Request Body에는 질문에 맞는 답변 목록을 보내주세요.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공")
    })
    public BaseResponse<BodyTypeAnalysisResponse> analyzeBodyType(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @RequestBody BodyTypeAnalysisRequest request
    ) {
        return BaseResponse.onSuccess(memberBodyTypeCommandService.analyzeBodyType(customUserDetails.getMember(), request.getAnswer()));
    }

    @GetMapping()
    @Operation(summary = "체형 질문 문항 조회 API - 프론트와 협의 필요(API 연동 안 해도 됨)",
            description = "체형 분석을 하기 위해 질문 문항을 받아 오는 API입니다. 이 부분은 서버에서 바로 프롬프트로 넣는 방법도 있기 때문에 프론트와 협의 후 진행하겠습니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공")
    })
    public BaseResponse<Void> getQuestion() {
        return BaseResponse.onSuccess(null);
    }

    @GetMapping("/result")
    @Operation(summary = "체형 분석 결과물 조회 API", description = "사용자의 체형 분석 결과물을 받아 오는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공")
    })
    public BaseResponse<BodyTypeAnalysisResponse> getBodyType(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return BaseResponse.onSuccess(memberBodyTypeQueryService.getBodyTypeAnalysis(customUserDetails.getMember()));
    }
}