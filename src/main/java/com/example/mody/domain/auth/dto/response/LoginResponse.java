package com.example.mody.domain.auth.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class LoginResponse {
	private Long memberId;
	private String nickname;
	private boolean isNewMember;        // 신규 회원 여부
	private boolean isRegistrationCompleted;  // 회원가입 완료 여부
}