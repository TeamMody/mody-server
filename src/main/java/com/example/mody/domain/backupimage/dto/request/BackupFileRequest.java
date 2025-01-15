package com.example.mody.domain.backupimage.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class BackupFileRequest {

    @Schema(
            description = "파일명",
            example = "test.jpg"
    )
    @NotBlank(message = "파일명 입력은 필수입니다.")
    private String fileName;

    @Schema(
            description = "파일 크기. 단위는 KB",
            example = "200"
    )
    @NotBlank(message = "파일 크기 입력은 필수입니다.")
    @Positive(message = "파일 크기는 음수일 수 없습니다.")
    private Long fileSize;


    @Schema(
            description = "s3 URI",
            example = "https://mody-s3-bucket.s3.ap-northeast-2.amazonaws.com/filea8500f7a-c902-4f64-b605-7bc6247b4e75]"
    )
    @NotBlank(message = "파일 주소 입력은 필수입니다.")
    private String s3Url;
}
