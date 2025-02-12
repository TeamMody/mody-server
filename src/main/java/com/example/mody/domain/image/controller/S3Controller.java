package com.example.mody.domain.image.controller;

import com.example.mody.domain.auth.security.CustomUserDetails;
import com.example.mody.domain.image.dto.request.PostPresignedUrlRequest;
import com.example.mody.domain.image.dto.request.ProfilePresignedUrlRequest;
import com.example.mody.domain.image.dto.response.PresignedUrlResponse;
import com.example.mody.domain.image.service.S3Service;
import com.example.mody.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "S3 API", description = "S3 사진 업로드 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/image")
public class S3Controller {

    private final S3Service s3Service;

    @PostMapping(value = "/upload/posts")
    @Operation(summary = "게시글 presigned url 생성 API", description = "게시글 파일 업로드(put) presigned url을 생성하는 API 입니다.")
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
    public BaseResponse<List<PresignedUrlResponse>> getPostPresignedUrl(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @RequestBody PostPresignedUrlRequest request
    ) {
        List<PresignedUrlResponse> presignedUrlResponse = s3Service.getPostPresignedUrls(customUserDetails.getMember().getId(), request.getFilenames());
        return BaseResponse.onSuccess(presignedUrlResponse);
    }

    @PostMapping(value = "/upload/profiles")
    @Operation(summary = "프로필 사진 presigned url 생성 API", description = "프로필 사진 파일 업로드(put) presigned url을 생성하는 API 입니다.")
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
    public BaseResponse<PresignedUrlResponse> getProfilePresignedUrl(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @RequestBody ProfilePresignedUrlRequest request
    ) {
        PresignedUrlResponse presignedUrlResponse = s3Service.getProfilePresignedUrl(customUserDetails.getMember().getId(), request.getFilename());
        return BaseResponse.onSuccess(presignedUrlResponse);
    }

//    // 테스트용(실제 사용 X)
//    @GetMapping(value = "/getS3Url")
//    @Operation(summary = "자체 S3 URL 조회 - 테스트용으로 API 연동 시 신경 안 써도 되는 API 입니다.",
//            description = "프론트에서 S3에 파일 업로드 후 반환하는 S3 URL을 서버에서 생성해 확인해보는 테스트용 API 입니다.")
//    @ApiResponses({
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",description = "S3 url 반환 성공")
//    })
//    public BaseResponse<S3UrlResponse> getS3Url(@RequestParam String key) {
//        S3UrlResponse s3UrlResponse = s3Service.getS3Url(key);
//        return BaseResponse.onSuccess(s3UrlResponse);
//    }

}
