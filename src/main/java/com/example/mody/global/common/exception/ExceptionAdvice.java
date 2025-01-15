package com.example.mody.global.common.exception;

import com.example.mody.global.common.base.BaseResponse;
import com.example.mody.global.common.exception.code.BaseCodeDto;
import com.example.mody.global.common.exception.code.status.GlobalErrorStatus;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestControllerAdvice(annotations = {RestController.class})
public class ExceptionAdvice extends ResponseEntityExceptionHandler {
    /*
     * 직접 정의한 RestApiException 에러 클래스에 대한 예외 처리
     */
    // @ExceptionHandler는 Controller계층에서 발생하는 에러를 잡아서 메서드로 처리해주는 기능
    @ExceptionHandler(value = RestApiException.class)
    public ResponseEntity<BaseResponse<String>> handleRestApiException(RestApiException e) {
        BaseCodeDto errorCode = e.getErrorCode();
        return handleExceptionInternal(errorCode);
    }

    /*
     * 일반적인 서버 에러에 대한 예외 처리
     */
    @ExceptionHandler
    public ResponseEntity<BaseResponse<String>> handleException(Exception e) {
        e.printStackTrace(); //예외 정보 출력

        return handleExceptionInternalFalse(GlobalErrorStatus._INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
    }

    /*
     * ConstraintViolationException 발생 시 예외 처리
     * 메서드 파라미터, 또는 메서드 리턴 값에 문제가 있을 경우, @Validated 검증 실패한 경우
     */
    @ExceptionHandler
    public ResponseEntity<BaseResponse<String>> handleConstraintViolationException(ConstraintViolationException e) {
        return handleExceptionInternal(GlobalErrorStatus._VALIDATION_ERROR.getCode());
    }

    /*
     * MethodArgumentTypeMismatchException 발생 시 예외 처리
     * 메서드의 인자 타입이 예상과 다른 경우
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<BaseResponse<String>> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException e) {
        // 예외 처리 로직
        return handleExceptionInternal(GlobalErrorStatus._METHOD_ARGUMENT_ERROR.getCode());
    }

    /*
     *  MethodArgumentNotValidException 발생 시 예외 처리
     * @@RequestBody 내부에서 처리 실패한 경우, @Valid 검증 실패한 경우 (ArgumnetResolver에 의해 유효성 검사)
     */
    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        Map<String, String> errors = new LinkedHashMap<>();

        e.getBindingResult().getFieldErrors().stream()
                .forEach(fieldError -> {
                    String fieldName = fieldError.getField();
                    String errorMessage = Optional.ofNullable(fieldError.getDefaultMessage()).orElse("");
                    errors.merge(fieldName, errorMessage, (existingErrorMessage, newErrorMessage) -> existingErrorMessage + ", " + newErrorMessage);
                });

        return handleExceptionInternalArgs(GlobalErrorStatus._VALIDATION_ERROR.getCode(), errors);

    }

    // 인증되지 않은 사용자에 대한 처리
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<BaseResponse<String>> handleAuthenticationException(AuthenticationException e) {
        return handleExceptionInternal(GlobalErrorStatus._UNAUTHORIZED.getCode());
    }

    // 권한이 없는 사용자에 대한 처리
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<BaseResponse<String>> handleAccessDeniedException(AccessDeniedException e) {
        return handleExceptionInternal(GlobalErrorStatus._ACCESS_DENIED.getCode());
    }


    private ResponseEntity<BaseResponse<String>> handleExceptionInternal(BaseCodeDto errorCode) {
        return ResponseEntity
                .status(errorCode.getHttpStatus().value())
                .body(BaseResponse.onFailure(errorCode.getCode(), errorCode.getMessage(), null));
    }

    private ResponseEntity<Object> handleExceptionInternalArgs(BaseCodeDto errorCode, Map<String, String> errorArgs) {
        return ResponseEntity
                .status(errorCode.getHttpStatus().value())
                .body(BaseResponse.onFailure(errorCode.getCode(), errorCode.getMessage(), errorArgs));
    }

    private ResponseEntity<BaseResponse<String>> handleExceptionInternalFalse(BaseCodeDto errorCode, String errorPoint) {
        return ResponseEntity
                .status(errorCode.getHttpStatus().value())
                .body(BaseResponse.onFailure(errorCode.getCode(), errorCode.getMessage(), errorPoint));
    }
}
