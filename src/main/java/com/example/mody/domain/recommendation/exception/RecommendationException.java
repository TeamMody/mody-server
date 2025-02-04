package com.example.mody.domain.recommendation.exception;

import com.example.mody.global.common.exception.RestApiException;
import com.example.mody.global.common.exception.code.BaseCodeInterface;

public class RecommendationException extends RestApiException {

    public RecommendationException(BaseCodeInterface errorCode) {
        super(errorCode);
    }
}
