package com.example.mody.domain.post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import static com.example.mody.domain.post.constant.PostConstant.POST_CONTENT_LIMIT;

@Schema(description = "게시글 수정 DTO")
@Getter
@NoArgsConstructor
public class PostUpdateRequest {

    @Schema(
            description = "내용",
            example = "스트레이트형 착장 예시"
    )
    @NotBlank(message = "content 는 필수입니다.")
    @Length(max = POST_CONTENT_LIMIT, message = "메세지의 최대 길이 {max}를 초과했습니다.")
    private String content;


    @Schema(
            description = "공개여부",
            example = "true"
    )
    @NotNull(message = "공개여부 입력은 필수입니다.")
    private Boolean isPublic;

}
