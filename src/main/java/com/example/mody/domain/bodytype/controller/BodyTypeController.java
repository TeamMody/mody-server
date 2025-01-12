package com.example.mody.domain.bodytype.controller;

import com.example.mody.domain.bodytype.dto.BodyTypeAnalysisResponse;
import com.example.mody.domain.chatgpt.service.ChatGptService;
import com.example.mody.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "체형 분석", description = "체형 분석 API")
@RestController
@RequestMapping("/body-analysis")
@RequiredArgsConstructor
public class BodyTypeController {

    private final ChatGptService chatGptService;

    @PostMapping("/result")
    @Operation(summary = "체형 분석 API", description = "OpenAi를 사용해서 체형을 분석하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "name", description = "사용자 이름", required = true),
            @Parameter(name = "gender", description = "사용자 성별", required = true)
    })
    public BaseResponse<BodyTypeAnalysisResponse> analyzeBodyType(
            @RequestParam @NotBlank(message = "query string으로, 이름을 필수로 입력해 주세요!") String name,
            @RequestParam @NotBlank(message = "query string으로, 성별을 필수로 입력해 주세요!") String gender,
            @RequestBody @NotBlank(message = "request body로, 질문에 맞는 답변 목록을 String으로 보내주세요!") String answers
    ) {
        return BaseResponse.onSuccess(chatGptService.analyzeBodyType(name, gender, answers));
    }
}