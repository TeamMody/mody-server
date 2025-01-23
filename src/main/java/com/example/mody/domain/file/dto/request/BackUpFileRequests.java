package com.example.mody.domain.file.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import static com.example.mody.domain.post.constant.PostConstant.POST_IMAGE_COUNT_LIMIT;

@NoArgsConstructor
@Getter
public class BackUpFileRequests {

    @Schema(
            description = "파일명, 파일 크기, S3 URL 목록",
            example = "[{" +
                    "\"fileName\": \"test.jpg\"," +
                    "\"fileSize\": 200," +
                    "\"s3Url\": \"https://mody-s3-bucket.s3.ap-northeast-2.amazonaws.com/filea8500f7a-c902-4f64-b605-7bc6247b4e75\"" +
                    "}, {" +
                    "\"fileName\": \"example.png\"," +
                    "\"fileSize\": 150," +
                    "\"s3Url\": \"https://mody-s3-bucket.s3.ap-northeast-2.amazonaws.com/fileb8500f7a-c902-4f64-b605-7bc6247b4e76\"" +
                    "}]"
    )
    @Size(max = POST_IMAGE_COUNT_LIMIT, message = "파일의 최대 개수는 {max}를 초과할 수 없습니다.")
    private List<BackupFileRequest> files;
}
