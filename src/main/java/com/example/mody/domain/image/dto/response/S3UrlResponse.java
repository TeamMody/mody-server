package com.example.mody.domain.image.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * S3 url 응답 DTO
 */
@Getter
@Builder
@AllArgsConstructor
@Schema(description = "S3 url 응답 DTO")
public class S3UrlResponse {

    @Schema(description = "S3 url", example = "https://{bucket-name}.s3.{region}.amazonaws.com/{object-key}")
    private String s3Url;

    public static S3UrlResponse from(String s3Url){
        return S3UrlResponse.builder()
                .s3Url(s3Url)
                .build();
    }
}