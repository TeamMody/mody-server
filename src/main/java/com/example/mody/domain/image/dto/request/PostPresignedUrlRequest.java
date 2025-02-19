package com.example.mody.domain.image.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 게시글 S3 presigned url 요청 DTO
 */
@Getter
@NoArgsConstructor
public class PostPresignedUrlRequest {
    @Schema(description = "업로드할 파일 목록", example = "[\"a.png\", \"b.jpg\"]")
    private List<String> filenames;
}