package com.example.mody.domain.auth.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mody.domain.auth.dto.request.EmailRequest;
import com.example.mody.domain.auth.dto.request.EmailVerificationRequest;
import com.example.mody.domain.auth.dto.request.MemberJoinRequest;
import com.example.mody.domain.auth.dto.request.MemberLoginReqeust;
import com.example.mody.domain.auth.dto.request.MemberRegistrationRequest;
import com.example.mody.domain.auth.dto.response.AccessTokenResponse;
import com.example.mody.domain.auth.dto.response.LoginResponse;
import com.example.mody.domain.auth.security.CustomUserDetails;
import com.example.mody.domain.auth.service.AuthCommandService;
import com.example.mody.domain.auth.service.email.EmailService;
import com.example.mody.domain.member.service.MemberCommandService;
import com.example.mody.global.common.base.BaseResponse;
import com.example.mody.global.common.exception.RestApiException;
import com.example.mody.global.common.exception.code.status.AuthErrorStatus;

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

	private static final Logger log = LoggerFactory.getLogger(AuthController.class);
	private final AuthCommandService authCommandService;
	private final MemberCommandService memberCommandService;
	private final EmailService emailService;

	@Operation(
		summary = "회원가입 완료",
		description = """
			소셜 로그인 혹은 자체 회원가입 후 추가 정보를 입력받아 회원가입을 완료합니다.
			- 카카오 로그인 시, 로그인 성공 후 신규 회원인 경우 호출해야하는 API입니다.
			- 자체 회원 가입 시, /auth/signup 성공 후 /auth/login으로 로그인 후 호출해야하는 API입니다.
			(자체 회원가입 API에서는 이메일, 비밀번호만 입력받고 현재 API를 호출하여 회원가입을 완료합니다.)
			"""
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
			responseCode = "COMMON401",
			description = "로그인하지 않은 경우에 발생합니다.(엑세스 토큰을 넣지 않았을 때)",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					value = """
								{
								  "timestamp": "2025-02-17T22:23:22.7640118",
								  "code": "COMMON401",
								  "message": "인증이 필요합니다."
								}
							"""
				)
			)
		)
	})
	@PostMapping("/signup/complete")
	public BaseResponse<Void> completeRegistration(
		@AuthenticationPrincipal CustomUserDetails customUserDetails,
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
		memberCommandService.completeRegistration(customUserDetails.getMember(), request);
		return BaseResponse.onSuccess(null);
	}

	@Operation(
		summary = "토큰 재발급",
		description = """
			Access Token이 만료되었을 때 Refresh Token을 사용하여 새로운 토큰을 발급받습니다.
			Refresh Token은 쿠키에서 자동으로 추출되며, 새로운 Access Token과 Refresh Token이 발급됩니다.
			발급된 Access Token은 응답 헤더의 Authorization에, Refresh Token은 쿠키에 포함됩니다.
			"""
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
			responseCode = "REFRESH_TOKEN404",
			description = "유효하지 않은 리프레쉬 토큰일 때 발생합니다. (디비에 동일한 리프레쉬 토큰이 없을 때 발생)",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					value = """
						{
						    "timestamp": "2024-01-13T10:00:00",
						    "code": "REFRESH_TOKEN404",
						    "message": "REFRESH TOKEN이 유효하지 않습니다.",
						    "result": null
						}
						"""
				)
			)
		)
	})
	@PostMapping("/reissue")
	public BaseResponse<AccessTokenResponse> reissueToken(
		@CookieValue(name = "refresh_token") String refreshToken,
		HttpServletResponse response
	) {
		log.info("refreshToken: {}", refreshToken);
		return BaseResponse.onSuccess(authCommandService.reissueToken(refreshToken, response));
	}

	@Operation(
		summary = "로그아웃",
		description = """
			사용자를 로그아웃 처리합니다.
			서버에서 Refresh Token을 삭제하고, 클라이언트의 쿠키에서도 Refresh Token을 제거합니다.
			클라이언트에서는 저장된 Access Token도 함께 삭제해야 합니다.
			""",
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
			responseCode = "COMMON401",
			description = "인증되지 않은 사용자",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					value = """
						{
								  "timestamp": "2025-02-17T22:23:22.7640118",
								  "code": "COMMON401",
								  "message": "인증이 필요합니다."
								}
						"""
				)
			)
		),
		@ApiResponse(
			responseCode = "REFRESH_TOKEN404",
			description = "유효하지 않은 Refresh Token",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					value = """
						{
						    "timestamp": "2024-01-13T10:00:00",
						    "code": "REFRESH_TOKEN404",
						    "message": "REFRESH TOKEN이 유효하지 않습니다.",
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
			.sameSite("None")
			.maxAge(0)
			.path("/")
			.build();

		response.setHeader("Set-Cookie", refreshTokenCookie.toString());
		return BaseResponse.onSuccess(null);
	}

	@Operation(
		summary = "일반 회원가입 API",
		description = "이메일과 비밀번호를 사용하여 새로운 회원을 등록합니다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "회원가입 성공",
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
			responseCode = "COMMON400",
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
			responseCode = "MEMBER409",
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
		),
		@ApiResponse(
			responseCode = "COMMON402",
			description = "올바르지 않은 비밀번호 형식",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					value = """
							{
								"timestamp": "2025-01-27T00:08:57.1421127",
								"code": "COMMON402",
								"message": "Validation Error입니다.",
								"result": {
									"password": "비밀번호는 8자 이상, 영어와 숫자, 그리고 특수문자(@$!%*?&#)를 포함해야 하며, 한글은 사용할 수 없습니다."
								}
							}
						"""
				)
			)
		)
	})
	@PostMapping("/signup")
	public BaseResponse<LoginResponse> joinMember(
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
		) MemberJoinRequest request,
		HttpServletResponse response
	) {
		LoginResponse loginResponse = memberCommandService.joinMember(request, response);
		return BaseResponse.onSuccess(loginResponse);
	}

	/**
	 * Swagger 명세를 위한 테스트 controller
	 * 실제 동작은 이 controller를 거치지 않고 JwtLoginFilter를 통해 이루어집니다.
	 * @param loginRequest
	 * @return
	 */
	@Operation(
		summary = "로그인 API",
		description = "이메일과 비밀번호를 사용하여 로그인합니다. 성공 시 Access Token과 Refresh Token이 발급됩니다."
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
			responseCode = "AUTH_INVALID_PASSWORD",
			description = "올바르지 않은 비밀번호를 입력함",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					value = """
						{
						    "timestamp": "2024-01-13T10:00:00",
						    "code": "AUTH_INVALID_PASSWORD",
						    "message": "비밀번호가 올바르지 않습니다."
						}
						"""
				)
			)
		),
		@ApiResponse(
			responseCode = "MEMBER404",
			description = "입력한 이메일을 가진 사용자가 없을 때 발생",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					value = """
							{
								"timestamp": "2025-01-27T00:12:53.6087824",
								"code": "MEMBER404",
								"message": "해당 회원을 찾을 수 없습니다."
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
		) MemberLoginReqeust loginRequest
	) {

		return BaseResponse.onSuccess(null);
	}

	@Operation(summary = "인증 메일 발송", description = "입력된 이메일로 인증번호를 발송합니다.")
	@PostMapping("/email/verify/send")
	public BaseResponse<Void> sendVerificationEmail(@Valid @RequestBody EmailRequest request) {
		emailService.sendVerificationEmail(request.getEmail());
		return BaseResponse.onSuccess(null);
	}

	@Operation(summary = "이메일 인증", description = "발송된 인증번호를 확인합니다.")
	@PostMapping("/email/verify")
	public BaseResponse<Void> verifyEmail(@Valid @RequestBody EmailVerificationRequest request) {
		if (!emailService.verifyEmail(request.getEmail(), request.getVerificationCode())) {
			throw new RestApiException(AuthErrorStatus.INVALID_VERIFICATION_CODE);
		}
		return BaseResponse.onSuccess(null);
	}

}