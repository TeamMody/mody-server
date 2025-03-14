package com.example.mody.domain.image.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * S3 presigned url 응답 DTO
 */
@Getter
@Builder
@AllArgsConstructor
@Schema(description = "S3 presigned url 응답 DTO")
public class PresignedUrlResponse {

    @Schema(description = "S3 presigned url", example = "https://{bucket-name}.s3.{region}.amazonaws.com/{object-key}?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date={timestamp}&X-Amz-SignedHeaders=host&X-Amz-Expires={expires}&X-Amz-Credential={credential}&X-Amz-Signature={signature}")
    private String presignedUrl;
    @Schema(description = "S3 key 값", example = "{folder}/{memberId}/{uuid}/{filename}")
    private String key;

    public static PresignedUrlResponse of(String preSignedUrl, String key) {
        return PresignedUrlResponse.builder()
                .presignedUrl(preSignedUrl)
                .key(key)
                .build();
    }
}
