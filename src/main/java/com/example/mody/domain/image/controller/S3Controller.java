package com.example.mody.domain.image.controller;

import com.example.mody.domain.auth.security.CustomUserDetails;
import com.example.mody.domain.image.dto.request.PostPresignedUrlRequest;
import com.example.mody.domain.image.dto.request.ProfilePresignedUrlRequest;
import com.example.mody.domain.image.dto.response.PresignedUrlResponse;
import com.example.mody.domain.image.service.S3Service;
import com.example.mody.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
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
public class S3Controller implements S3ControllerInterface {

    private final S3Service s3Service;

    @PostMapping(value = "/upload/posts")
    @Operation(summary = "게시글 presigned url 생성 API", description = "게시글 파일 업로드(put) presigned url을 생성하는 API 입니다.")
    public BaseResponse<List<PresignedUrlResponse>> getPostPresignedUrl(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @RequestBody PostPresignedUrlRequest request
    ) {
        List<PresignedUrlResponse> presignedUrlResponse = s3Service.getPostPresignedUrls(customUserDetails.getMember().getId(), request.getFilenames());
        return BaseResponse.onSuccess(presignedUrlResponse);
    }

    @PostMapping(value = "/upload/profiles")
    @Operation(summary = "프로필 사진 presigned url 생성 API", description = "프로필 사진 파일 업로드(put) presigned url을 생성하는 API 입니다.")
    public BaseResponse<PresignedUrlResponse> getProfilePresignedUrl(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @Valid @RequestBody ProfilePresignedUrlRequest request
    ) {
        PresignedUrlResponse presignedUrlResponse = s3Service.getProfilePresignedUrl(customUserDetails.getMember().getId(), request.getFilename());
        return BaseResponse.onSuccess(presignedUrlResponse);
    }
}
