package com.example.mody.domain.post.dto.request;

import static com.example.mody.domain.post.constant.PostConstant.*;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Schema(description = "게시글 작성 DTO")
@Getter
@NoArgsConstructor
public class PostCreateRequest {

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

	@Schema(
		description = "S3 url 목록",
		example = "[\"https://{bucket-name}.s3.{region}.amazonaws.com/{object-key1}\", " +
				"\"https://my-bucket.s3.amazonaws.com/path/to/file.jpg\"]"
	)
	@NotEmpty(message = "S3 url 목록은 비어 있을 수 없습니다.")
  @Size(max = POST_IMAGE_COUNT_LIMIT, message = "파일의 최대 개수는 {max}를 초과할 수 없습니다.")
	private List<String> s3Urls;

}
