package com.example.mody.domain.image.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 프로필 사진 S3 presigned url 요청 DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class ProfilePresignedUrlRequest {
    @Schema(description = "업로드할 파일", example = "a.png")
    private String filename;
}