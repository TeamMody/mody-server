package com.example.mody.domain.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
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
		description = """
			카카오 로그인 프로세스를 시작합니다.
			프론트엔드에서는 이 URL로 리다이렉트하여 카카오 로그인을 시작해야 합니다.
			스웨거에서는 리다이렉트를 지원하지 않기 때문에, 프론트엔드에서 직접 리다이렉트해야 합니다.
			예시: window.location.href = 'http://your-domain/oauth2/authorization/kakao';
			""",
		tags = {"소셜 로그인"}
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "302",
			description = "카카오 로그인 페이지로 리다이렉트",
			headers = @Header(
				name = "Location",
				description = "카카오 로그인 페이지 URL",
				schema = @Schema(type = "string")
			)
		)
	})
	@GetMapping("/authorization/kakao")
	public void kakaoLogin() {
		// Spring Security에서 자동으로 처리
	}

	@Hidden // 실제 구현이 필요없는 콜백 엔드포인트는 문서에서 숨김
	@GetMapping("/callback/kakao")
	public void kakaoCallback(@RequestParam String code) {
		// Spring Security에서 자동으로 처리
	}
}