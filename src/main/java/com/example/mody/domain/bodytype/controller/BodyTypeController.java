package com.example.mody.domain.bodytype.controller;

import com.example.mody.domain.auth.security.CustomUserDetails;
import com.example.mody.domain.bodytype.dto.request.BodyTypeAnalysisRequest;
import com.example.mody.domain.bodytype.dto.response.BodyTypeAnalysisResponse;
import com.example.mody.domain.bodytype.service.memberbodytype.MemberBodyTypeCommandService;
import com.example.mody.domain.bodytype.service.memberbodytype.MemberBodyTypeQueryService;
import com.example.mody.domain.style.dto.response.StyleRecommendResponse;
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
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "체형 분석 성공"),
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
    public BaseResponse<BodyTypeAnalysisResponse> analyzeBodyType(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @RequestBody BodyTypeAnalysisRequest request
    ) {
        return BaseResponse.onSuccess(memberBodyTypeCommandService.analyzeBodyType(customUserDetails.getMember(), request.getAnswer()));
    }

//    @GetMapping()
//    @Operation(summary = "체형 질문 문항 조회 API - 프론트와 협의 필요(API 연동 안 해도 됨)",
//            description = "체형 분석을 하기 위해 질문 문항을 받아 오는 API입니다. 이 부분은 서버에서 바로 프롬프트로 넣는 방법도 있기 때문에 프론트와 협의 후 진행하겠습니다.")
//    @ApiResponses({
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공")
//    })
//    public BaseResponse<Void> getQuestion() {
//        return BaseResponse.onSuccess(null);
//    }

    @GetMapping("/result")
    @Operation(summary = "체형 분석 결과 조회 API", description = "사용자의 체형 분석 결과를 받아 오는 API입니다.")
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
    public BaseResponse<BodyTypeAnalysisResponse> getBodyType(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return BaseResponse.onSuccess(memberBodyTypeQueryService.getBodyTypeAnalysis(customUserDetails.getMember()));
    }
}