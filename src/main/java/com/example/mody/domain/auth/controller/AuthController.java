package com.example.mody.domain.auth.controller;

import org.springframework.http.ResponseCookie;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mody.domain.auth.dto.request.MemberJoinRequest;
import com.example.mody.domain.auth.dto.request.MemberLoginReqeust;
import com.example.mody.domain.auth.dto.request.MemberRegistrationRequest;
import com.example.mody.domain.auth.security.CustomUserDetails;
import com.example.mody.domain.auth.service.AuthCommandService;
import com.example.mody.domain.member.service.MemberCommandService;
import com.example.mody.global.common.base.BaseResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Auth API", description = "인증 관련 API - 회원가입, 로그인, 토큰 재발급, 로그아웃 등의 기능을 제공합니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

	private final AuthCommandService authCommandService;
	private final MemberCommandService memberCommandService;

	@Operation(
		summary = "소셜 로그인 회원가입 완료",
		description = """
			소셜 로그인 후 추가 정보를 입력받아 회원가입을 완료합니다.
			카카오 로그인 성공 후 신규 회원인 경우 호출해야 하는 API입니다.
			""",
		tags = {"회원가입"}
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "회원가입 완료 성공",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = BaseResponse.class),
				examples = @ExampleObject(
					value = """
						{
						    "timestamp": "2024-01-13T10:00:00",
						    "code": "COMMON200",
						    "message": "요청에 성공하였습니다.",
						    "result": null
						}
						"""
				)
			)
		),
		@ApiResponse(
			responseCode = "400",
			description = "잘못된 요청",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					value = """
						{
						    "timestamp": "2024-01-13T10:00:00",
						    "code": "COMMON400",
						    "message": "필수 정보가 누락되었습니다.",
						    "result": null
						}
						"""
				)
			)
		),
		@ApiResponse(
			responseCode = "404",
			description = "사용자를 찾을 수 없음",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					value = """
						{
						    "timestamp": "2024-01-13T10:00:00",
						    "code": "MEMBER404",
						    "message": "해당 회원을 찾을 수 없습니다.",
						    "result": null
						}
						"""
				)
			)
		)
	})
	@PostMapping("/signup/oauth2")
	public BaseResponse<Void> completeRegistration(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@Valid @RequestBody
		@Parameter(
			description = "회원가입 완료 요청 정보",
			required = true,
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = MemberRegistrationRequest.class),
				examples = @ExampleObject(
					value = """
						{
						    "nickname": "사용자닉네임",
						    "birthDate": "2000-01-01",
						    "gender": "MALE",
						    "height": 180,
						    "profileImageUrl": "https://example.com/profile.jpg"
						}
						"""
				)
			)
		) MemberRegistrationRequest request
	) {
		memberCommandService.completeRegistration(userDetails.getMember().getId(), request);
		return BaseResponse.onSuccess(null);
	}

	@Operation(
		summary = "토큰 재발급",
		description = """
			Access Token이 만료되었을 때 Refresh Token을 사용하여 새로운 토큰을 발급받습니다.
			Refresh Token은 쿠키에서 자동으로 추출되며, 새로운 Access Token과 Refresh Token이 발급됩니다.
			발급된 Access Token은 응답 헤더의 Authorization에, Refresh Token은 쿠키에 포함됩니다.
			""",
		tags = {"인증", "토큰"}
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "토큰 재발급 성공",
			headers = {
				@Header(
					name = "Authorization",
					description = "새로 발급된 Access Token",
					schema = @Schema(type = "string", example = "Bearer eyJhbGciOiJIUzI1...")
				),
				@Header(
					name = "Set-Cookie",
					description = "새로 발급된 Refresh Token (HttpOnly Cookie)",
					schema = @Schema(type = "string", example = "refresh_token=eyJhbGciOiJIUzI1...; Path=/; HttpOnly; Secure; SameSite=Strict")
				)
			},
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = BaseResponse.class),
				examples = @ExampleObject(
					value = """
						{
						    "timestamp": "2024-01-13T10:00:00",
						    "code": "COMMON200",
						    "message": "요청에 성공하였습니다.",
						    "result": null
						}
						"""
				)
			)
		),
		@ApiResponse(
			responseCode = "401",
			description = "유효하지 않거나 만료된 Refresh Token",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					value = """
						{
						    "timestamp": "2024-01-13T10:00:00",
						    "code": "AUTH006",
						    "message": "유효하지 않은 REFRESH TOKEN입니다.",
						    "result": null
						}
						"""
				)
			)
		)
	})
	@PostMapping("/reissue")
	public BaseResponse<Void> reissueToken(
		@CookieValue(name = "refresh_token")
		@Parameter(
			description = "리프레시 토큰 (쿠키에서 자동 추출)",
			required = true
		) String refreshToken,
		HttpServletResponse response
	) {
		authCommandService.reissueToken(refreshToken, response);
		return BaseResponse.onSuccess(null);
	}

	@Operation(
		summary = "로그아웃",
		description = """
			사용자를 로그아웃 처리합니다.
			서버에서 Refresh Token을 삭제하고, 클라이언트의 쿠키에서도 Refresh Token을 제거합니다.
			클라이언트에서는 저장된 Access Token도 함께 삭제해야 합니다.
			""",
		tags = {"인증"},
		security = @SecurityRequirement(name = "Bearer Authentication")
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "로그아웃 성공",
			headers = {
				@Header(
					name = "Set-Cookie",
					description = "Refresh Token 제거를 위한 쿠키",
					schema = @Schema(type = "string", example = "refresh_token=; Path=/; Max-Age=0; HttpOnly; Secure; SameSite=Strict")
				)
			},
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = BaseResponse.class),
				examples = @ExampleObject(
					value = """
						{
						    "timestamp": "2024-01-13T10:00:00",
						    "code": "COMMON200",
						    "message": "요청에 성공하였습니다.",
						    "result": null
						}
						"""
				)
			)
		),
		@ApiResponse(
			responseCode = "401",
			description = "인증되지 않은 사용자",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					value = """
						{
						    "timestamp": "2024-01-13T10:00:00",
						    "code": "AUTH001",
						    "message": "JWT가 없습니다.",
						    "result": null
						}
						"""
				)
			)
		),
		@ApiResponse(
			responseCode = "400",
			description = "유효하지 않은 Refresh Token",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					value = """
						{
						    "timestamp": "2024-01-13T10:00:00",
						    "code": "AUTH006",
						    "message": "유효하지 않은 REFRESH TOKEN입니다.",
						    "result": null
						}
						"""
				)
			)
		)
	})
	@PostMapping("/logout")
	public BaseResponse<Void> logout(
		@CookieValue(name = "refresh_token")
		@Parameter(
			description = "리프레시 토큰 (쿠키에서 자동 추출)",
			required = true
		) String refreshToken,
		HttpServletResponse response
	) {
		authCommandService.logout(refreshToken);

		ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh_token", "")
			.httpOnly(true)
			.secure(true)
			.sameSite("Strict")
			.maxAge(0)
			.path("/")
			.build();

		response.setHeader("Set-Cookie", refreshTokenCookie.toString());
		return BaseResponse.onSuccess(null);
	}

	@Operation(
		summary = "일반 회원가입 API",
		description = "이메일과 비밀번호를 사용하여 새로운 회원을 등록합니다.",
		tags = {"회원가입"}
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "회원가입 성공",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = BaseResponse.class)
			)
		),
		@ApiResponse(
			responseCode = "400",
			description = "잘못된 요청 데이터",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					value = """
						{
						    "timestamp": "2024-01-13T10:00:00",
						    "code": "COMMON400",
						    "message": "유효하지 않은 이메일 형식입니다.",
						    "result": null
						}
						"""
				)
			)
		),
		@ApiResponse(
			responseCode = "409",
			description = "이미 존재하는 이메일",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					value = """
						{
						    "timestamp": "2024-01-13T10:00:00",
						    "code": "MEMBER409",
						    "message": "이미 등록된 이메일입니다.",
						    "result": null
						}
						"""
				)
			)
		)
	})
	@PostMapping("/signup")
	public BaseResponse<Void> joinMember(
		@Valid @RequestBody
		@Parameter(
			description = "회원가입 요청 정보",
			required = true,
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = MemberJoinRequest.class),
				examples = @ExampleObject(
					value = """
						{
						    "email": "user@example.com",
						    "password": "Password123!",
						    "nickname": "사용자닉네임"
						}
						"""
				)
			)
		) MemberJoinRequest request
	) {
		memberCommandService.joinMember(request);
		return BaseResponse.onSuccess(null);
	}

	/**
	 * Swagger 명세를 위한 API
	 * @param loginReqeust
	 * @return
	 */
	@Operation(
		summary = "로그인 API",
		description = "이메일과 비밀번호를 사용하여 로그인합니다. 성공 시 Access Token과 Refresh Token이 발급됩니다.",
		tags = {"인증", "로그인"}
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "로그인 성공",
			headers = {
				@io.swagger.v3.oas.annotations.headers.Header(
					name = "Authorization",
					description = "Access Token",
					schema = @Schema(type = "string", example = "Bearer eyJhbGciOiJIUzI1...")
				),
				@io.swagger.v3.oas.annotations.headers.Header(
					name = "Set-Cookie",
					description = "Refresh Token (HttpOnly Cookie)",
					schema = @Schema(type = "string")
				)
			},
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = BaseResponse.class)
			)
		),
		@ApiResponse(
			responseCode = "401",
			description = "로그인 실패",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					value = """
						{
						    "timestamp": "2024-01-13T10:00:00",
						    "code": "AUTH401",
						    "message": "이메일 또는 비밀번호가 일치하지 않습니다.",
						    "result": null
						}
						"""
				)
			)
		)
	})
	@PostMapping("/login")
	public BaseResponse<Void> authLogin(
		@RequestBody @Valid
		@Parameter(
			description = "로그인 요청 정보",
			required = true,
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = MemberLoginReqeust.class),
				examples = @ExampleObject(
					value = """
						{
						    "email": "user@example.com",
						    "password": "Password123!"
						}
						"""
				)
			)
		) MemberLoginReqeust loginReqeust
	) {

		return BaseResponse.onSuccess(null);
	}
}