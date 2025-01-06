package com.example.mody.domain.exception;

import com.example.mody.global.common.exception.RestApiException;
import com.example.mody.global.common.exception.code.BaseCodeInterface;

public class MemberException extends RestApiException {

	public MemberException(BaseCodeInterface errorCode) {
		super(errorCode);
	}
}
