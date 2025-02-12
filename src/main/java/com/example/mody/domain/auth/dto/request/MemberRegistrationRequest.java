package com.example.mody.domain.auth.dto.request;

import java.time.LocalDate;

import com.example.mody.domain.member.enums.Gender;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원 가입할 때 필요한 정보들
 */
@Schema(description = "회원가입 요청 DTO")
@Getter
@NoArgsConstructor
public class MemberRegistrationRequest {

	@Schema(
		description = "닉네임",
		example = "모디",
		minLength = 2,
		maxLength = 20
	)
	@NotBlank(message = "닉네임은 필수입니다")
	private String nickname;

	@Schema(
		description = "생년월일",
		example = "1990-01-01",
		type = "string",
		format = "date"
	)
	@NotNull(message = "생년월일은 필수입니다")
	@Past(message = "생년월일은 과거 날짜여야 합니다")
	private LocalDate birthDate;

	@Schema(
		description = "성별",
		example = "MALE",
		allowableValues = {"MALE", "FEMALE"}
	)
	@NotNull(message = "성별은 필수입니다")
	private Gender gender;

	@Schema(
		description = "키(cm)",
		example = "170",
		minimum = "100",
		maximum = "250"
	)
	@NotNull(message = "키는 필수입니다")
	@Positive(message = "키는 양수여야 합니다")
	private Integer height;

	@Schema(
		description = "프로필 이미지 URL",
		example = "https://my-bucket.s3.amazonaws.com/path/to/file.jpg",
		required = false
	)
	private String profileImageUrl;
}