package com.example.mody.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry corsRegistry) {
		corsRegistry.addMapping("/**")
				.exposedHeaders("Set-Cookie")
				.allowedOriginPatterns("*")
//				.allowedOrigins(
//						"http://localhost:5173",   // 현재 요청 URL
//						"https://kkoalla.app",
//						"https://kkoalla.app:5173",
//						"https://kkoalla.app:8443"
//				)
				.allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
				.allowedHeaders("*")
				.allowCredentials(true);
	}
}