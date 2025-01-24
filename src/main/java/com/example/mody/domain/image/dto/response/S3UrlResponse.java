package com.example.mody.domain.image.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "S3 Url 응답 DTO")
public class S3UrlResponse {

    @Schema(description = "S3 url", example = "https://{bucket-name}.s3.{region}.amazonaws.com/{object-key}")
    private String s3Url;

    public static S3UrlResponse from(String s3Url){
        S3UrlResponse response = new S3UrlResponse();
        response.setS3Url(s3Url);
        return response;
    }
}