package com.example.mody.global.common.exception.code.status;

import com.example.mody.global.common.exception.code.BaseCodeDto;
import com.example.mody.global.common.exception.code.BaseCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BodyTypeErrorStatus implements BaseCodeInterface {

    MEMBER_BODY_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER_BODY_TYPE404", "체형 분석 결과를 찾을 수 없습니다."),
    BODY_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "BODY_TYPE404", "체형을 찾을 수 없습니다."),
    JSON_PARSING_ERROR(HttpStatus.BAD_REQUEST, "JSON_PARSING400", "체형 분석 결과를 처리하는 중 JSON 파싱에 실패했습니다."),
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
