package com.example.mody.domain.exception;

import com.example.mody.global.common.exception.RestApiException;
import com.example.mody.global.common.exception.code.BaseCodeInterface;

public class PostException extends RestApiException {
    public PostException(BaseCodeInterface errorCode) {
        super(errorCode);
    }
}
