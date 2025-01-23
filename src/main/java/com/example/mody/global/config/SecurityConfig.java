package com.example.mody.global.config;

import java.util.Arrays;
import java.util.Collections;

import com.example.mody.domain.auth.jwt.JwtLoginFilter;
import com.example.mody.domain.auth.service.AuthCommandService;
import com.example.mody.domain.auth.service.AuthCommandServiceImpl;
import org.springframework.boot.web.servlet.ServletContextInitializer;
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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
			.authorizeHttpRequests(authz -> authz
				.requestMatchers("/auth/**", "/oauth2/**").permitAll()
				.requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
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
		configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "Accept"));
		configuration.setExposedHeaders(Arrays.asList("Authorization", "Set-Cookie"));
		configuration.setAllowCredentials(true);
		configuration.setMaxAge(3600L);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter(jwtProvider, memberRepository, objectMapper, memberQueryService);
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