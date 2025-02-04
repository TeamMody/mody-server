package com.example.mody.domain.auth.handler;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.mody.domain.auth.jwt.JwtProvider;
import com.example.mody.domain.auth.security.CustomOAuth2User;
import com.example.mody.domain.auth.service.AuthCommandService;
import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.member.enums.Role;
import com.example.mody.domain.member.enums.Status;
import com.example.mody.domain.member.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

	private final JwtProvider jwtProvider;
	private final MemberRepository memberRepository;
	private final AuthCommandService authCommandService;

	// 프론트 엔드 주소, 환경변수에서 주입
	@Value("${front.redirect-url}")
	private String FRONT_REDIRECT_URL;

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
		String newAccessToken = authCommandService.processLoginSuccess(member, response);

		// 리다이렉션 URL 생성
		String targetUrl = UriComponentsBuilder.fromUriString(FRONT_REDIRECT_URL)
				.queryParam("memberId", member.getId())
				.queryParam("nickname", URLEncoder.encode(member.getNickname(), StandardCharsets.UTF_8))
				.queryParam("newMember", isNewMember)
				.queryParam("isRegistrationCompleted", member.isRegistrationCompleted())
				.build().toUriString();

		// 리다이렉션 수행
		response.sendRedirect(targetUrl);

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