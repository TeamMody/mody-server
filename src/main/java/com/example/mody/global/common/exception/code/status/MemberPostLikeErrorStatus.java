package com.example.mody.global.common.exception.code.status;

import ch.qos.logback.core.spi.ErrorCodes;
import com.example.mody.global.common.exception.code.BaseCodeDto;
import com.example.mody.global.common.exception.code.BaseCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberPostLikeErrorStatus implements BaseCodeInterface {
    LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER_POST_LIKE404", "해당 좋아요를 찾을 수 없습니다.")
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
