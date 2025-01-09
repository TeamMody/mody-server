package com.example.mody.domain.auth.service;

import com.example.mody.domain.auth.dto.request.MemberJoinRequest;
import com.example.mody.domain.exception.MemberException;
import com.example.mody.domain.member.converter.MemberConverter;
import com.example.mody.global.common.exception.code.status.MemberErrorStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.mody.domain.auth.entity.RefreshToken;
import com.example.mody.domain.auth.jwt.JwtProvider;
import com.example.mody.domain.auth.repository.RefreshTokenRepository;
import com.example.mody.domain.exception.RefreshTokenException;
import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.member.repository.MemberRepository;
import com.example.mody.global.common.exception.RestApiException;
import com.example.mody.global.common.exception.code.status.AuthErrorStatus;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthCommandServiceImpl implements AuthCommandService {

	private final JwtProvider jwtProvider;
	private final RefreshTokenRepository refreshTokenRepository;

	public void reissueToken(String oldRefreshToken, HttpServletResponse response) {
		// 기존 Refresh Token 검증
		RefreshToken refreshTokenEntity = refreshTokenRepository.findByToken(oldRefreshToken)
			.orElseThrow(() -> new RefreshTokenException(AuthErrorStatus.INVALID_REFRESH_TOKEN));

		// Refresh Token에 해당하는 회원 조회
		Member member = refreshTokenEntity.getMember();

		// 새로운 토큰 발급
		String newAccessToken = jwtProvider.createAccessToken(member.getProviderId());
		String newRefreshToken = jwtProvider.createRefreshToken(member.getProviderId());

		// Refresh Token 교체 (Rotation)
		refreshTokenEntity.updateToken(newRefreshToken);

		// Refresh Token을 쿠키에 설정
		ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh_token", newRefreshToken)
			.httpOnly(true)
			.secure(true)
			.sameSite("Strict")
			.maxAge(7 * 24 * 60 * 60) // 7일
			.path("/")
			.build();

		// Access Token을 Authorization 헤더에 설정
		response.setHeader("Authorization", "Bearer " + newAccessToken);
		response.setHeader("Set-Cookie", refreshTokenCookie.toString());
	}

	public void saveRefreshToken(Member member, String refreshToken) {
		// 기존 리프레시 토큰이 있다면 업데이트, 없다면 새로 생성
		RefreshToken refreshTokenEntity = refreshTokenRepository.findByMember(member)
			.orElse(RefreshToken.builder()
				.member(member)
				.token(refreshToken)
				.build());

		refreshTokenEntity.updateToken(refreshToken);
		refreshTokenRepository.save(refreshTokenEntity);
	}

	public void logout(String refreshToken) {
		RefreshToken refreshTokenEntity = refreshTokenRepository.findByToken(refreshToken)
			.orElseThrow(() -> new RestApiException(AuthErrorStatus.INVALID_REFRESH_TOKEN));

		refreshTokenRepository.delete(refreshTokenEntity);
	}
}