package com.example.mody.domain.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "로그인 응답 DTO")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class LoginResponse {
	@Schema(
		description = "회원 ID",
		example = "1"
	)
	private Long memberId;

	@Schema(
		description = "닉네임",
		example = "모디"
	)
	private String nickname;

	@Schema(
		description = "신규 회원 여부",
		example = "true"
	)
	private boolean isNewMember;

	@Schema(
		description = "회원가입 완료 여부",
		example = "false"
	)
	private boolean isRegistrationCompleted;

	@Schema(
		description = "access 토큰"
	)
	private String accessToken;

	public static LoginResponse of(Long memberId, String nickname, boolean isNewMember,
		boolean isRegistrationCompleted, String newAccessToken) {
		return LoginResponse.builder()
			.memberId(memberId)
			.nickname(nickname)
			.isNewMember(isNewMember)
			.isRegistrationCompleted(isRegistrationCompleted)
			.accessToken(newAccessToken)
			.build();
	}
}