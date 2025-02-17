package com.example.mody.global.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.mody.domain.auth.handler.OAuth2SuccessHandler;
import com.example.mody.domain.auth.jwt.JwtAuthenticationFilter;
import com.example.mody.domain.auth.jwt.JwtLoginFilter;
import com.example.mody.domain.auth.jwt.JwtProvider;
import com.example.mody.domain.auth.security.OAuth2UserService;
import com.example.mody.domain.auth.service.AuthCommandService;
import com.example.mody.domain.member.repository.MemberRepository;
import com.example.mody.domain.member.service.MemberQueryService;
import com.example.mody.global.util.CustomAuthenticationEntryPoint;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final OAuth2UserService oAuth2UserService;
	private final OAuth2SuccessHandler oAuth2SuccessHandler;
	private final JwtProvider jwtProvider;
	private final MemberRepository memberRepository;
	private final ObjectMapper objectMapper;
	private final AuthenticationConfiguration authenticationConfiguration;
	private final AuthCommandService authCommandService;
	private final MemberQueryService memberQueryService;
	private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		// 기본 보안 설정
		http
			.csrf(csrf -> csrf.disable())
			.sessionManagement(session -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.httpBasic(httpBasic -> httpBasic.disable())
			.authorizeHttpRequests(authz -> authz
				.requestMatchers("/auth/signup/complete").authenticated()
				.requestMatchers("/auth/**", "/oauth2/**").permitAll()
				.requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
				.requestMatchers("/email/**").permitAll()
				.anyRequest().authenticated()
			)
			.oauth2Login(oauth2 -> oauth2
				.userInfoEndpoint(userInfo -> userInfo
					.userService(oAuth2UserService)
				)
				.successHandler(oAuth2SuccessHandler)
			)
			.exceptionHandling(exceptions -> exceptions
				.authenticationEntryPoint(customAuthenticationEntryPoint)
			)
			.cors(cors -> cors
				.configurationSource(corsConfigurationSource())
			)
			.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

		// JWT 로그인 필터 설정
		JwtLoginFilter jwtLoginFilter = new JwtLoginFilter(
			authenticationManager(authenticationConfiguration),
			jwtProvider,
			authCommandService,
			memberRepository,
			objectMapper
		);
		jwtLoginFilter.setFilterProcessesUrl("/auth/login");
		http.addFilterAt(jwtLoginFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();

		// 허용할 Origin 설정
//		configuration.setAllowedOrigins(Arrays.asList(
//			"https://kkoalla.app",        // 프론트엔드 도메인
//			"https://kkoalla.app:5173",   // 개발 서버
//			"http://localhost:5173",       // 로컬 개발
//			"https://kkoalla.app:8443"
//		));
		configuration.addAllowedOriginPattern("*");

		// 허용할 HTTP 메서드 설정
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

		// 허용할 헤더 설정
		configuration.setAllowedHeaders(Arrays.asList(
			"Authorization",
			"Content-Type",
			"X-Requested-With",
			"Accept",
			"Origin",
			"Access-Control-Request-Method",
			"Access-Control-Request-Headers",
			"Access-Control-Allow-Origin"
		));

		// 인증 정보 포함 설정
		configuration.setAllowCredentials(true);

		// preflight 요청의 캐시 시간 설정
		configuration.setMaxAge(3600L);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter(jwtProvider, memberRepository, objectMapper);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}
}
