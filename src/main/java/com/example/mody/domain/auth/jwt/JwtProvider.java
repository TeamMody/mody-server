package com.example.mody.domain.auth.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.mody.global.common.exception.RestApiException;
import com.example.mody.global.common.exception.code.status.AuthErrorStatus;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

/**
 * JWT 토큰 생성 및 검증을 담당하는 클래스
 */
@Slf4j
@Component
public class JwtProvider {

	private final SecretKey secretKey;
	private final long accessTokenValidityInMilliseconds;
	private final long refreshTokenValidityInMilliseconds;

	public JwtProvider(
		// secret key
		@Value("${jwt.secret}") final String secretKey,
		// access token 유효 시간
		@Value("${jwt.accessExpiration}") final long accessTokenValidityInMilliseconds,
		// refresh token 유효 시간
		@Value("${jwt.refreshExpiration}") final long refreshTokenValidityInMilliseconds
	) {
		this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
		this.accessTokenValidityInMilliseconds = accessTokenValidityInMilliseconds;
		this.refreshTokenValidityInMilliseconds = refreshTokenValidityInMilliseconds;
	}

	// access token 생성
	public String createAccessToken(String subject) {
		return createToken(subject, accessTokenValidityInMilliseconds);
	}

	// refresh token 생성
	public String createRefreshToken(String subject) {
		return createToken(subject, refreshTokenValidityInMilliseconds);
	}

	// 토큰 생성
	private String createToken(String subject, long validityInMilliseconds) {
		Date now = new Date();
		Date validity = new Date(now.getTime() + validityInMilliseconds);

		return Jwts.builder()
			.subject(subject)
			.issuedAt(now)
			.expiration(validity)
			.signWith(secretKey)
			.compact();
	}

	// 토큰 검증 및 subject 반환
	public String validateTokenAndGetSubject(String token) {
		try {
			return Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.getSubject();
		} catch (ExpiredJwtException e) {
			throw new RestApiException(AuthErrorStatus.EXPIRED_MEMBER_JWT);
		} catch (UnsupportedJwtException e) {
			throw new RestApiException(AuthErrorStatus.UNSUPPORTED_JWT);
		} catch (Exception e) {
			throw new RestApiException(AuthErrorStatus.INVALID_ACCESS_TOKEN);
		}
	}
}
