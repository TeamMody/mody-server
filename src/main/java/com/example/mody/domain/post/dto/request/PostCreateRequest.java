package com.example.mody.domain.post.dto.request;

import com.example.mody.domain.backupimage.dto.request.BackupFileRequest;
import com.example.mody.domain.bodytype.entity.BodyType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.List;

import static com.example.mody.domain.post.constant.PostConstant.POST_CONTENT_LIMIT;

@Schema(description = "게시글 작성 DTO")
@Getter
@NoArgsConstructor
public class PostCreateRequest {

    @Schema(
            description = "내용",
            example = "스트레이트형 착장 예시"
    )
    @NotBlank (message = "content 는 필수입니다.")
    @Length(max = POST_CONTENT_LIMIT, message = "메세지의 최대 길이 {max}를 초과했습니다.")
    private String content;


    @Schema(
            description = "공개여부",
            example = "true"
    )
    @NotNull(message = "공개여부 입력은 필수입니다.")
    private Boolean isPublic;


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
    private List<BackupFileRequest> files;
}
