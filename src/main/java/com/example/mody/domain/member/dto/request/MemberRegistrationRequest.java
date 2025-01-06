package com.example.mody.domain.member.dto;

import java.time.LocalDate;

import com.example.mody.domain.member.enums.Gender;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberRegistrationRequest {
	@NotNull(message = "OAuth2 제공자의 고유 ID는 필수입니다")
	private Long memberId;

	@NotBlank(message = "닉네임은 필수입니다")
	private String nickname;

	@NotNull(message = "생년월일은 필수입니다")
	@Past(message = "생년월일은 과거 날짜여야 합니다")
	private LocalDate birthDate;

	@NotNull(message = "성별은 필수입니다")
	private Gender gender;

	@NotNull(message = "키는 필수입니다")
	@Positive(message = "키는 양수여야 합니다")
	private Integer height;

	private String profileImageUrl;
}