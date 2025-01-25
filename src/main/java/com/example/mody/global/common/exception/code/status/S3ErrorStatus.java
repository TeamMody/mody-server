package com.example.mody.global.common.exception.code.status;

import com.example.mody.global.common.exception.code.BaseCodeDto;
import com.example.mody.global.common.exception.code.BaseCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum S3ErrorStatus implements BaseCodeInterface {

	// 버킷 관련 오류
	BUCKET_NOT_FOUND(HttpStatus.NOT_FOUND, "S3_404", "지정된 S3 버킷을 찾을 수 없습니다."),
	BUCKET_ACCESS_DENIED(HttpStatus.FORBIDDEN, "S3_403", "S3 버킷에 대한 접근이 거부되었습니다."),
	BUCKET_NAME_INVALID(HttpStatus.BAD_REQUEST, "S3_400", "S3 버킷 이름이 유효하지 않습니다."),
	BUCKET_REGION_MISMATCH(HttpStatus.BAD_REQUEST, "S3_400", "요청한 S3 버킷이 지정된 리전과 일치하지 않습니다."),

	// 객체(파일) 관련 오류
	OBJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "S3_404", "요청한 S3 객체를 찾을 수 없습니다."),
	OBJECT_ACCESS_DENIED(HttpStatus.FORBIDDEN, "S3_403", "S3 객체에 대한 접근이 거부되었습니다."),
	OBJECT_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "S3_500", "S3 객체 업로드 중 오류가 발생했습니다."),
	OBJECT_DOWNLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "S3_500", "S3 객체 다운로드 중 오류가 발생했습니다."),
	OBJECT_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "S3_500", "S3 객체 삭제 중 오류가 발생했습니다."),
	OBJECT_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "S3_400", "업로드할 파일 크기가 허용된 한도를 초과했습니다."),
	OBJECT_INVALID_KEY(HttpStatus.BAD_REQUEST, "S3_400", "S3 객체 키가 유효하지 않습니다."),

	// presigned url 관련 오류
	PRESIGNED_URL_GENERATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "S3_500", "S3 Presigned URL 생성 중 오류가 발생했습니다."),
	PRESIGNED_URL_EXPIRED(HttpStatus.BAD_REQUEST, "S3_400", "요청한 Presigned URL이 만료되었습니다."),
	PRESIGNED_URL_INVALID(HttpStatus.BAD_REQUEST, "S3_400", "요청한 Presigned URL이 유효하지 않습니다."),

	// 인증 및 권한 관련 오류
	CREDENTIALS_INVALID(HttpStatus.UNAUTHORIZED, "S3_401", "S3 접근을 위한 자격 증명이 유효하지 않습니다."),
	PERMISSION_DENIED(HttpStatus.FORBIDDEN, "S3_403", "S3 작업에 대한 권한이 부족합니다."),

	// 기타 S3 관련 오류
	NETWORK_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S3_500", "S3 서비스와의 네트워크 연결에 문제가 발생했습니다."),
	UNKNOWN_S3_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S3_500", "알 수 없는 S3 오류가 발생했습니다."),
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
