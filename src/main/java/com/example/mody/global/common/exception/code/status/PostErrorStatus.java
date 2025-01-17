package com.example.mody.global.common.exception.code.status;

import org.springframework.http.HttpStatus;

import com.example.mody.global.common.exception.code.BaseCodeDto;
import com.example.mody.global.common.exception.code.BaseCodeInterface;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PostErrorStatus implements BaseCodeInterface {
	
	POST_NOT_FOUND(HttpStatus.NOT_FOUND, "POST404", "해당 게시글을 찾을 수 없습니다."),
	POST_ALREADY_REPORT(HttpStatus.BAD_REQUEST, "POST400", "이미 신고한 게시글 입니다."),
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
