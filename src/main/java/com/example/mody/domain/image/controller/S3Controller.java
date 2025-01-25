package com.example.mody.domain.image.controller;

import com.example.mody.domain.auth.security.CustomUserDetails;
import com.example.mody.domain.image.dto.request.PresignedUrlRequest;
import com.example.mody.domain.image.dto.response.PresignedUrlResponse;
import com.example.mody.domain.image.dto.response.S3UrlResponse;
import com.example.mody.domain.image.service.S3Service;
import com.example.mody.domain.post.service.PostQueryService;
import com.example.mody.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
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

    @PostMapping(value = "/upload")
    @Operation(summary = "presigned url 생성 API", description = "파일 업로드(put) presigned url을 생성하는 API 입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공")
    })
    public BaseResponse<List<PresignedUrlResponse>> getPresignedUrl(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @RequestBody PresignedUrlRequest request
    ) {
        List<PresignedUrlResponse> presignedUrlResponse = s3Service.getPresignedUrls(customUserDetails.getMember().getId(), request.getFilenames());
        return BaseResponse.onSuccess(presignedUrlResponse);
    }

    // 테스트용(실제 사용 X)
    @GetMapping(value = "/getS3Url")
    @Operation(summary = "자체 S3 URL 조회 - 테스트용으로 API 연동 시 신경 안 써도 되는 API 입니다.",
            description = "프론트에서 S3에 파일 업로드 후 반환하는 S3 URL을 서버에서 생성해 확인해보는 테스트용 API 입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공")
    })
    public BaseResponse<S3UrlResponse> getS3Url(@RequestParam String key) {
        S3UrlResponse s3UrlResponse = s3Service.getS3Url(key);
        return BaseResponse.onSuccess(s3UrlResponse);
    }

}
