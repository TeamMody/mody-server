package com.example.mody.domain.auth.service;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.mody.domain.auth.dto.response.AccessTokenResponse;
import com.example.mody.domain.auth.entity.RefreshToken;
import com.example.mody.domain.auth.jwt.JwtProvider;
import com.example.mody.domain.auth.repository.RefreshTokenRepository;
import com.example.mody.domain.exception.RefreshTokenException;
import com.example.mody.domain.member.entity.Member;
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

	public AccessTokenResponse reissueToken(String oldRefreshToken, HttpServletResponse response) {
		log.info("Client refresh token: {}", oldRefreshToken);
		RefreshToken refreshTokenEntity = refreshTokenRepository.findByToken(oldRefreshToken)
			.orElseThrow(() -> {
				log.warn("DB에 저장된 refresh token과 일치하는 값이 없습니다.");
				return new RefreshTokenException(AuthErrorStatus.INVALID_REFRESH_TOKEN);
			});
		log.info("DB refresh token for member {}: {}", refreshTokenEntity.getMember().getId(),
			refreshTokenEntity.getToken());
		
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
			.secure(false)
			.maxAge(7 * 24 * 60 * 60) // 7일
			.path("/")
			.build();

		// Access Token을 Authorization 헤더에 설정

		response.setHeader("Set-Cookie", refreshTokenCookie.toString());

		return AccessTokenResponse.builder()
			.accessToken(newAccessToken)
			.build();
	}

	public void saveRefreshToken(Member member, String refreshToken) {
		// 기존 리프레시 토큰이 있다면 업데이트, 없다면 새로 생성
		RefreshToken refreshTokenEntity = refreshTokenRepository.findByMember(member)
			.orElse(RefreshToken.builder()
				.member(member)
				.token(refreshToken)
				.build());

		refreshTokenEntity.updateToken(refreshToken);

	}

	public void logout(String refreshToken) {
		RefreshToken refreshTokenEntity = refreshTokenRepository.findByToken(refreshToken)
			.orElseThrow(() -> new RestApiException(AuthErrorStatus.INVALID_REFRESH_TOKEN));

		refreshTokenRepository.delete(refreshTokenEntity);
	}

	/**
	 * 로그인 성공 시, 엑세스 토큰과 리프레쉬 토큰을 발급하고 헤더에 넣는 코드를 공통으로 묶음.
	 */
	@Override
	public String processLoginSuccess(Member member, HttpServletResponse response) {
		// Access Token, Refresh Token 발급
		String newAccessToken = jwtProvider.createAccessToken(member.getId().toString());
		String newRefreshToken = jwtProvider.createRefreshToken(member.getId().toString());

		// Refresh Token 저장
		saveRefreshToken(member, newRefreshToken);

		// Refresh Token을 쿠키에 설정
		ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh_token", newRefreshToken)
			.httpOnly(true)
			.secure(false)
			.sameSite("None")
			.maxAge(30) // 7일(7 * 24 * 60 * 60)
			.path("/")
			.build();

		response.setHeader("Set-Cookie", refreshTokenCookie.toString());

		return newAccessToken;
	}
}