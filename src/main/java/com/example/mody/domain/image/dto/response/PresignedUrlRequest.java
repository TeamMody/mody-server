package com.example.mody.domain.image.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * S3 presigned url 요청 DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class PresignedUrlRequest {
    @Schema(description = "업로드할 파일 목록", example = "[\"a.png\", \"b.jpg\"]")
    private List<String> filenames;
}