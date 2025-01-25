package com.example.mody.domain.auth.dto.response;

import com.example.mody.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "로그인 응답 DTO")
@Getter
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

	public static LoginResponse of(Long memberId, String nickname, boolean isNewMember, boolean isRegistrationCompleted){
		return new LoginResponse(memberId, nickname, isNewMember, isRegistrationCompleted);
	}
}