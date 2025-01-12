package com.example.mody.domain.exception;

import com.example.mody.global.common.exception.RestApiException;
import com.example.mody.global.common.exception.code.BaseCodeInterface;

public class BodyTypeException extends RestApiException {
    public BodyTypeException(BaseCodeInterface errorCode) {
        super(errorCode);
    }
}
