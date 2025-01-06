package com.example.mody.domain.auth.controller;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mody.domain.auth.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Tag(name = "Auth API", description = "인증 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

	private final AuthService authService;

	@Operation(summary = "토큰 재발급 API", description = "Refresh Token으로 새로운 Access Token을 발급받는 API")
	@PostMapping("/reissue")
	public ResponseEntity<Void> reissueToken(@CookieValue(name = "refresh_token") String refreshToken,
		HttpServletResponse response) {
		authService.reissueToken(refreshToken, response);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "로그아웃 API", description = "로그아웃하고 Refresh Token을 제거하는 API")
	@PostMapping("/logout")
	public ResponseEntity<Void> logout(@CookieValue(name = "refresh_token") String refreshToken,
		HttpServletResponse response) {
		authService.logout(refreshToken);

		// 쿠키 삭제
		ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh_token", "")
			.httpOnly(true)
			.secure(true)
			.sameSite("Strict")
			.maxAge(0)
			.path("/")
			.build();

		response.setHeader("Set-Cookie", refreshTokenCookie.toString());
		return ResponseEntity.ok().build();
	}
}