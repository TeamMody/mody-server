package com.example.mody.domain.style.exception;

import com.example.mody.global.common.exception.RestApiException;
import com.example.mody.global.common.exception.code.BaseCodeInterface;

public class StyleException extends RestApiException {
	public StyleException(BaseCodeInterface errorCode) {
		super(errorCode);
	}
}
