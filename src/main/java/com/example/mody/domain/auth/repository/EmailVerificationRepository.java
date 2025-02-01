package com.example.mody.domain.auth.repository;

import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class EmailVerificationRepository {

	private static final String KEY_PREFIX = "email:verification:";
	private final StringRedisTemplate redisTemplate;

	public void saveVerificationCode(String email, String code, Duration duration) {
		redisTemplate.opsForValue()
			.set(KEY_PREFIX + email, code, duration);
	}

	public String getVerificationCode(String email) {
		return redisTemplate.opsForValue().get(KEY_PREFIX + email);
	}

	public void removeVerificationCode(String email) {
		redisTemplate.delete(KEY_PREFIX + email);
	}

	public boolean hasKey(String email) {
		return Boolean.TRUE.equals(redisTemplate.hasKey(KEY_PREFIX + email));
	}
}