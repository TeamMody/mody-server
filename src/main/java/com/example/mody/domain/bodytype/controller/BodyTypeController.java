package com.example.mody.domain.bodytype.controller;

import com.example.mody.domain.auth.security.CustomUserDetails;
import com.example.mody.domain.bodytype.dto.request.BodyTypeAnalysisRequest;
import com.example.mody.domain.bodytype.dto.response.BodyTypeAnalysisResponse;
import com.example.mody.domain.bodytype.service.memberbodytype.MemberBodyTypeCommandService;
import com.example.mody.domain.bodytype.service.memberbodytype.MemberBodyTypeQueryService;
import com.example.mody.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "체형 분석", description = "체형 분석 관련 API")
@RestController
@RequestMapping("/body-analysis")
@RequiredArgsConstructor
public class BodyTypeController implements BodyTypeControllerInterface {

    private final MemberBodyTypeCommandService memberBodyTypeCommandService;
    private final MemberBodyTypeQueryService memberBodyTypeQueryService;

    @PostMapping("/result")
	@Operation(summary = "체형 분석 API", description = "OpenAI를 사용해서 사용자의 체형을 분석하는 API입니다. Request Body에는 질문에 맞는 답변 목록을 보내주세요.")
    public BaseResponse<BodyTypeAnalysisResponse> analyzeBodyType(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @RequestBody BodyTypeAnalysisRequest request
    ) {
        return BaseResponse.onSuccess(memberBodyTypeCommandService.analyzeBodyType(customUserDetails.getMember(), request.getAnswer()));
    }

    @GetMapping("/result")
    @Operation(summary = "체형 분석 결과 조회 API", description = "사용자의 체형 분석 결과를 받아 오는 API입니다.")
    public BaseResponse<BodyTypeAnalysisResponse> getBodyType(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return BaseResponse.onSuccess(memberBodyTypeQueryService.getBodyTypeAnalysis(customUserDetails.getMember()));
    }
}