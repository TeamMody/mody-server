package com.example.mody.global.common.exception.code.status;

import org.springframework.http.HttpStatus;

import com.example.mody.global.common.exception.code.BaseCodeDto;
import com.example.mody.global.common.exception.code.BaseCodeInterface;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RecommendationErrorStatus implements BaseCodeInterface {

    RECOMMENDATION_NOT_FOUND(HttpStatus.NOT_FOUND, "RECOMMENDATION404", "해당 추천 결과를 찾을 수 없습니다."),
    STYLE_CATEGORY_EMPTY(HttpStatus.BAD_REQUEST, "RECOMMENDATION401", "데이터베이스에 스타일 카테고리가 입력되어 있지 않습니다."),
    APPEAL_CATEGORY_EMPTY(HttpStatus.NOT_FOUND, "RECOMMENDATION402", "테이터베이스에 어필 카테고리가 입력되어 있지 않습니다.");


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
