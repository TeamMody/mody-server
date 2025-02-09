package com.example.mody.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry corsRegistry) {
		corsRegistry.addMapping("/**")
				.allowedOrigins("*") // 프론트 URL 추가
				.allowedMethods("*") // 모든 HTTP 메서드 허용 (GET, POST, PATCH, DELETE, OPTIONS 등)
				.allowedHeaders("*") // 모든 헤더 허용
				.exposedHeaders("Authorization", "Set-Cookie") // JWT 토큰 등 노출
				.allowCredentials(true); // 인증 포함 허용 (JWT 사용 시 필수)
	}
}