package com.example.mody.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
@OpenAPIDefinition(
		servers = {
				@Server(url = "https://kkoalla.app:8443/dev", description = "모디 https 개발 서버입니다."),
				@Server(url = "https://kkoalla.app:8443/prod", description = "모디 https 배포 서버입니다."),
				@Server(url = "http://3.37.4.11:8000", description = "모디 http 개발 서버입니다."),
				@Server(url = "http://3.37.4.11:8080", description = "모디 http 배포 서버입니다."),
				@Server(url = "http://localhost:8080", description = "모디 local 서버입니다.")
		}
)
public class SwaggerConfig {
	@Bean
	public OpenAPI openAPI() {
		String jwtSchemeName = "Authorization";
		// API 요청헤더에 인증정보 포함
		SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);
		// SecuritySchemes 등록
		Components components = new Components()
			.addSecuritySchemes(jwtSchemeName, new SecurityScheme()
				.name(jwtSchemeName)
				.type(SecurityScheme.Type.HTTP)
				.scheme("Bearer")
				.bearerFormat("JWT"));

		return new OpenAPI()
			.addSecurityItem(securityRequirement)
			.components(components)
			.info(new Info()
				.title("MODY API Documentation")
				.description("""
					# 소셜 로그인 플로우
					                            
					## 1. 카카오 로그인
					1. 프론트엔드에서 카카오 로그인 버튼 클릭
					2. `/oauth2/authorization/kakao` 엔드포인트로 리다이렉트
					3. 카카오 로그인 페이지에서 인증
					4. 인증 성공 시 `/oauth2/callback/kakao?code={code}` 로 리다이렉트
					5. 서버에서 인증 코드로 카카오 토큰 발급
					6. 카카오 토큰으로 사용자 정보 조회
					7. DB에서 사용자 확인 후 처리:
					   - 기존 회원: JWT 토큰 발급
					   - 신규 회원: 회원가입 페이지로 이동
					                            
					## 2. 응답 형식
					- Authorization 헤더: Bearer JWT 액세스 토큰
					- Set-Cookie: httpOnly secure 리프레시 토큰
					- Body: LoginResponse (회원 정보)
					                            
					## 3. 토큰 갱신
					- 액세스 토큰 만료 시 `/auth/refresh` 호출
					- 리프레시 토큰으로 새로운 액세스 토큰 발급
					                            
					## 4. 로그아웃
					- `/auth/logout` 호출
					- 리프레시 토큰 삭제 및 쿠키 제거
					""")
				.version("1.0.0"));
	}
}