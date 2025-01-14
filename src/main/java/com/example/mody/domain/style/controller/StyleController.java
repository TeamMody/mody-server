package com.example.mody.domain.style.controller;

import com.example.mody.domain.style.dto.request.StyleRecommendRequest;
import com.example.mody.domain.style.dto.response.StyleRecommendResponse;
import com.example.mody.domain.style.service.StyleCommandService;
import com.example.mody.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/style-analysis")
public class StyleController {

    private final StyleCommandService styleCommandService;

    @Operation(summary = "스타일 추천 결과 반환 API", description = "스타일 분석 후 그 결과를 반환하는 API")
    @PostMapping("/result")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "스타일 추천 성공",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))
            )
    })
    public BaseResponse<StyleRecommendResponse> recommendStyle(
            @Valid @RequestBody StyleRecommendRequest request) {
        return BaseResponse.onSuccess(styleCommandService.recommendStyle(request));
    }
}
