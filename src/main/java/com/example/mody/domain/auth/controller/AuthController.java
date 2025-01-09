package com.example.mody.domain.auth.controller;

import com.example.mody.domain.auth.dto.request.MemberJoinRequest;
import com.example.mody.domain.auth.dto.request.MemberLoginReqeust;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.mody.domain.auth.dto.request.MemberRegistrationRequest;
import com.example.mody.domain.auth.service.AuthCommandService;
import com.example.mody.domain.member.service.MemberCommandService;
import com.example.mody.global.common.base.BaseResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Auth API", description = "인증 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

	private final AuthCommandService authCommandService;
	private final MemberCommandService memberCommandService;

	/**
	 * 회원가입 완료 API
	 * @param request
	 * @return
	 */
	@Operation(summary = "카카오로그인 회원가입 완료 API", description = "소셜 로그인 후 추가 정보를 입력받아 회원가입을 완료하는 API")
	@PostMapping("/signup/oauth2")
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "토큰 재발급 성공",
			content = @Content(schema = @Schema(implementation = BaseResponse.class))
		)
	})
	public BaseResponse<Void> completeRegistration(
		@Valid @RequestBody MemberRegistrationRequest request) {
		memberCommandService.completeRegistration(request);
		return BaseResponse.onSuccess(null);
	}

	/**
	 * 토큰 재발급 API
	 * @param refreshToken
	 * @param response
	 * @return
	 */
	@Operation(summary = "토큰 재발급 API", description = "Refresh Token으로 새로운 Access Token을 발급받는 API")
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "토큰 재발급 성공",
			content = @Content(schema = @Schema(implementation = BaseResponse.class))
		)
	})
	@Parameters({
		@Parameter(
			name = "refresh_token",
			description = "리프레시 토큰 (쿠키)",
			required = true,
			schema = @Schema(type = "string")
		)
	})
	@PostMapping("/reissue")
	public ResponseEntity<Void> reissueToken(@CookieValue(name = "refresh_token") String refreshToken,
		HttpServletResponse response) {

		// 토큰 재발급을 서비스 단에서 수행하도록 함.
		authCommandService.reissueToken(refreshToken, response);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "로그아웃 API", description = "로그아웃하고 Refresh Token을 제거하는 API")
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "로그아웃 성공",
			content = @Content(schema = @Schema(implementation = BaseResponse.class))
		)
	})
	@Parameters({
		@Parameter(
			name = "refresh_token",
			description = "리프레시 토큰 (쿠키)",
			required = true,
			schema = @Schema(type = "string")
		)
	})
	@PostMapping("/logout")
	public ResponseEntity<Void> logout(@CookieValue(name = "refresh_token") String refreshToken,
		HttpServletResponse response) {
		authCommandService.logout(refreshToken);

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

	@Operation(summary = "회원가입 API")
	@PostMapping("/signup")
	public BaseResponse<Void> joinMember(@Valid @RequestBody MemberJoinRequest request) {

		memberCommandService.joinMember(request);
		return BaseResponse.onSuccess(null);
	}

	@Operation(summary = "로그인 API", description = "사용자의 이메일과 비밀번호를 통해 JWT Access Token과 Refresh Token을 발급받습니다.")
	@PostMapping("/login")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "로그인 성공"),
			@ApiResponse(responseCode = "401", description = "잘못된 이메일 또는 비밀번호")
	})
	public ResponseEntity<Void> authLogin(@RequestBody @Valid MemberLoginReqeust loginReqeust) {
		return ResponseEntity.ok().build();
	}

}