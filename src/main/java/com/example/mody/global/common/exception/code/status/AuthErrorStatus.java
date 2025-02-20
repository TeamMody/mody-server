package com.example.mody.global.common.exception.code.status;

import org.springframework.http.HttpStatus;

import com.example.mody.global.common.exception.code.BaseCodeDto;
import com.example.mody.global.common.exception.code.BaseCodeInterface;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthErrorStatus implements BaseCodeInterface {

	EMPTY_JWT(HttpStatus.UNAUTHORIZED, "AUTH401", "JWT가 없습니다."),
	EXPIRED_MEMBER_JWT(HttpStatus.UNAUTHORIZED, "TOKEN402", "JWT가 만료되었습니다."),
	UNSUPPORTED_JWT(HttpStatus.UNAUTHORIZED, "TOKEN403", "지원하지 않는 JWT입니다."),
	INVALID_ID_TOKEN(HttpStatus.BAD_REQUEST, "TOKEN401", "유효하지 않은 ID TOKEN입니다."),

	EXPIRED_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "REFRESH_TOKEN401", "REFRESH TOKEN이 만료되었습니다."),
	INVALID_ACCESS_TOKEN(HttpStatus.BAD_REQUEST, "ACCESS_TOKEN404", "ACCESS TOKEN이 유효하지 않습니다."),
	INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "REFRESH_TOKEN404", "REFRESH TOKEN이 유효하지 않습니다."),
	FAILED_SOCIAL_LOGIN(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH007", "소셜 로그인에 실패하였습니다."),

	FAILED_GITHUB_AUTHENTICATION(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH008", "깃허브 서버와 통신이 실패하였습니다."),
	FAILED_GET_APPLE_KEY(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH009", "애플 서버와 통신이 실패하였습니다."),
	INVALID_APPLE_ID_TOKEN(HttpStatus.BAD_REQUEST, "AUTH010", "유효하지 않은 애플 ID TOKEN입니다."),

	// Email Error
	INVALID_VERIFICATION_CODE(HttpStatus.BAD_REQUEST, "AUTH011", "유효하지 않은 인증 코드입니다."),

	// 로그인 실패
	AUTHENTICATION_FAILED(HttpStatus.BAD_REQUEST, "LOGIN001", "아이디로 유저를 찾을 수 없습니다."),
	PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "LOGIN002", "비밀번호가 다름니다.")
	;

	private final HttpStatus httpStatus;
	private final boolean isSuccess = false;
	private final String code;
	private final String message;

	@Override
	public BaseCodeDto getCode() {
		return BaseCodeDto.builder()
			.httpStatus(httpStatus)
			.isSuccess(isSuccess)
			.code(code)
			.message(message)
			.build();
	}
}
