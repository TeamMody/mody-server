package com.example.mody.domain.image.controller;

import com.example.mody.domain.auth.security.CustomUserDetails;
import com.example.mody.domain.image.dto.request.PostPresignedUrlRequest;
import com.example.mody.domain.image.dto.request.ProfilePresignedUrlRequest;
import com.example.mody.domain.image.dto.response.PresignedUrlResponse;
import com.example.mody.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface S3ControllerInterface {

    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "presigned url 생성을 성공하였습니다."),
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
                    description = "S3 버킷을 찾을 수 없습니다.",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
						{
							"timestamp": "2025-01-26T15:15:54.334Z",
							"code": "S3_404",
							"message": "지정된 S3 버킷을 찾을 수 없습니다."
						}
						"""
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "presigned url 생성을 실패하였습니다.",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
						{
							"timestamp": "2025-01-26T15:15:54.334Z",
							"code": "S3_500",
							"message": "S3 presigned url 생성 중 오류가 발생했습니다."
						}
						"""
                            )
                    )
            ),
    })
    BaseResponse<List<PresignedUrlResponse>> getPostPresignedUrl(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @RequestBody PostPresignedUrlRequest request
    );

    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "presigned url 생성을 성공하였습니다."),
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
                    description = "S3 버킷을 찾을 수 없습니다.",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
						{
							"timestamp": "2025-01-26T15:15:54.334Z",
							"code": "S3_404",
							"message": "지정된 S3 버킷을 찾을 수 없습니다."
						}
						"""
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "presigned url 생성을 실패하였습니다.",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
						{
							"timestamp": "2025-01-26T15:15:54.334Z",
							"code": "S3_500",
							"message": "S3 presigned url 생성 중 오류가 발생했습니다."
						}
						"""
                            )
                    )
            ),
    })
    BaseResponse<PresignedUrlResponse> getProfilePresignedUrl(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @RequestBody ProfilePresignedUrlRequest request
    );
}
