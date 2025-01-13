package com.example.mody.domain.auth.handler;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.mody.domain.auth.dto.response.LoginResponse;
import com.example.mody.domain.auth.jwt.JwtProvider;
import com.example.mody.domain.auth.security.CustomOAuth2User;
import com.example.mody.domain.auth.service.AuthCommandService;
import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.member.enums.Role;
import com.example.mody.domain.member.enums.Status;
import com.example.mody.domain.member.repository.MemberRepository;
import com.example.mody.global.common.base.BaseResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

	private final JwtProvider jwtProvider;
	private final ObjectMapper objectMapper;
	private final MemberRepository memberRepository;
	private final AuthCommandService authCommandService;

	/**
	 * OAuth2 로그인 성공 시 처리
	 * @param request
	 * @param response
	 * @param authentication
	 * @throws IOException
	 * @throws ServletException
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {
		CustomOAuth2User oAuth2User = (CustomOAuth2User)authentication.getPrincipal();

		// 회원 조회 또는 생성
		Member member = memberRepository.findByProviderId(oAuth2User.getOAuth2Response().getProviderId())
			.orElseGet(() -> saveMember(oAuth2User));

		// 새로 가입한 멤버인지 아닌지 확인
		boolean isNewMember = member.getCreatedAt().equals(member.getUpdatedAt());

		// Access Token, Refresh Token 발급
		String accessToken = jwtProvider.createAccessToken(member.getProviderId());
		String refreshToken = jwtProvider.createRefreshToken(member.getProviderId());

		// Refresh Token 저장
		authCommandService.saveRefreshToken(member, refreshToken);

		// Refresh Token을 쿠키에 설정
		ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh_token", refreshToken)
			.httpOnly(true)
			.secure(true)
			.sameSite("Strict")
			.maxAge(7 * 24 * 60 * 60) // 7일
			.path("/")
			.build();

		// Access Token을 Authorization 헤더에 설정
		response.setHeader("Authorization", "Bearer " + accessToken);
		response.setHeader("Set-Cookie", refreshTokenCookie.toString());

		// 로그인 응답 데이터 설정
		LoginResponse loginResponse = LoginResponse.builder()
			.memberId(member.getId())
			.nickname(member.getNickname())
			.isNewMember(isNewMember)
			.isRegistrationCompleted(member.isRegistrationCompleted())
			.build();

		// 응답 바디 작성
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(objectMapper.writeValueAsString(BaseResponse.onSuccess(loginResponse)));
	}

	private Member saveMember(CustomOAuth2User oAuth2User) {
		Member member = Member.builder()
			.providerId(oAuth2User.getOAuth2Response().getProviderId())
			.provider(oAuth2User.getOAuth2Response().getProvider())
			.nickname(oAuth2User.getOAuth2Response().getName())
			.status(Status.ACTIVE)
			.role(Role.ROLE_USER)
			.isRegistrationCompleted(false)
			.build();

		return memberRepository.save(member);
	}
}