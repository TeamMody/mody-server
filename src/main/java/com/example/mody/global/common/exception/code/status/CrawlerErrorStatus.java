package com.example.mody.global.common.exception.code.status;

import com.example.mody.global.common.exception.code.BaseCodeDto;
import com.example.mody.global.common.exception.code.BaseCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CrawlerErrorStatus implements BaseCodeInterface {
    PAGE_LOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "PAGE500", "페이지를 불러오는 데 실패했습니다."),

    SEARCH_EXECUTION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SEARCH500", "검색 실행 중 오류가 발생했습니다."),

    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "IMAGE404", "검색 결과에서 이미지를 찾을 수 없습니다."),

    CHROME_DRIVER_VERSION_MISMATCH(HttpStatus.INTERNAL_SERVER_ERROR, "CHROME500", "ChromeDriver와 Chrome 브라우저 버전이 일치하지 않습니다."),

    SELENIUM_BLOCKED(HttpStatus.FORBIDDEN, "SELENIUM403", "Pinterest에서 Selenium 사용이 감지되었습니다. (크롤링 차단 가능성)"),

    CRAWLING_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "CRAWLING500", "크롤링 중 알 수 없는 오류가 발생했습니다."),
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