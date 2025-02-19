package com.example.mody.domain.bodytype.controller;

import com.example.mody.domain.auth.security.CustomUserDetails;
import com.example.mody.domain.bodytype.dto.request.BodyTypeAnalysisRequest;
import com.example.mody.domain.bodytype.dto.response.BodyTypeAnalysisResponse;
import com.example.mody.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;

public interface BodyTypeControllerInterface {

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "체형 분석 성공"),
            @ApiResponse(
                    responseCode = "401",
                    description = "Access Token이 필요합니다.",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
						{
							"timestamp": "2025-01-26T15:15:54.334Z",
							"code": "COMMON401",
							"message": "인증이 필요합니다."
						}
						"""
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "체형을 찾을 수 없습니다.",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
						{
							"timestamp": "2025-01-26T15:15:54.334Z",
							"code": "BODY_TYPE404",
							"message": "체형을 찾을 수 없습니다."
						}
						"""
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "GPT가 적절한 응답을 하지 못 했습니다.",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
						{
							"timestamp": "2025-01-26T15:15:54.334Z",
							"code": "ANALYSIS108",
							"message": "GPT가 올바르지 않은 답변을 했습니다. 관리자에게 문의하세요."
						}
						"""
                            )
                    )
            ),
    })
    BaseResponse<BodyTypeAnalysisResponse> analyzeBodyType(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @RequestBody BodyTypeAnalysisRequest request);

    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "체형 분석 결과 조회 성공"),
            @ApiResponse(
                    responseCode = "400",
                    description = "체형 분석 결과를 처리하는 중 JSON 파싱에 실패했습니다.",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
						{
							"timestamp": "2025-01-26T15:15:54.334Z",
							"code": "JSON_PARSING400",
							"message": "체형 분석 결과를 처리하는 중 JSON 파싱에 실패했습니다."
						}
						"""
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Access Token이 필요합니다.",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
						{
							"timestamp": "2025-01-26T15:15:54.334Z",
							"code": "COMMON401",
							"message": "인증이 필요합니다."
						}
						"""
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "체형 분석 결과를 찾을 수 없습니다.",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
						{
							"timestamp": "2025-01-26T15:15:54.334Z",
							"code": "MEMBER_BODY_TYPE404",
							"message": "체형 분석 결과를 찾을 수 없습니다."
						}
						"""
                            )
                    )
            ),
    })
    BaseResponse<BodyTypeAnalysisResponse> getBodyType(@AuthenticationPrincipal CustomUserDetails customUserDetails);
}
