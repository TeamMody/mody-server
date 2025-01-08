package com.example.mody.domain.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * OAuth2 관련 API
 * 스웨거 명세를 위해서 작성된 코드입니다.
 */
@Tag(name = "OAuth2", description = "소셜 로그인 관련 API")
@RestController
@RequestMapping("/oauth2")
public class OAuth2Controller {

	@Operation(
		summary = "카카오 로그인 시작",
		description = "카카오 로그인 페이지로 리다이렉트됩니다. " +
			"프론트엔드에서는 window.location.href를 사용하여 이 URL로 이동해야 합니다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "302",
			description = "카카오 로그인 페이지로 리다이렉트",
			content = @Content(
				mediaType = "text/html",
				examples = @ExampleObject(
					value = "카카오 로그인 페이지로 리다이렉트됩니다."
				)
			)
		)
	})
	@GetMapping("/authorization/kakao")
	public ResponseEntity<Void> kakaoLogin() {
		return ResponseEntity.ok().build();
	}

	@Operation(
		summary = "카카오 로그인 콜백",
		description = "카카오 인증 완료 후 리다이렉트되는 엔드포인트입니다. " +
			"인증 코드를 받아 처리 후, 액세스 토큰과 리프레시 토큰을 발급합니다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "로그인 성공",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = com.example.mody.domain.auth.dto.response.LoginResponse.class)
			)
		),
		@ApiResponse(
			responseCode = "401",
			description = "인증 실패"
		)
	})
	@Parameters({
		@Parameter(
			name = "code",
			description = "카카오에서 발급된 인증 코드",
			required = true,
			schema = @Schema(type = "string")
		)
	})
	@GetMapping("/callback/kakao")
	public ResponseEntity<Void> kakaoCallback(
		@RequestParam String code
	) {
		return ResponseEntity.ok().build();
	}
}