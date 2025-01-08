package com.example.mody.domain.exception;

import com.example.mody.global.common.exception.RestApiException;
import com.example.mody.global.common.exception.code.BaseCodeInterface;

public class RefreshTokenException extends RestApiException {

	public RefreshTokenException(BaseCodeInterface errorCode) {
		super(errorCode);
	}
}
