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

import com.example.mody.domain.auth.handler.OAuth2SuccessHandler;
import com.example.mody.domain.auth.jwt.JwtAuthenticationFilter;
import com.example.mody.domain.auth.jwt.JwtLoginFilter;
import com.example.mody.domain.auth.jwt.JwtProvider;
import com.example.mody.domain.auth.security.OAuth2UserService;
import com.example.mody.domain.auth.service.AuthCommandService;
import com.example.mody.domain.member.repository.MemberRepository;
import com.example.mody.domain.member.service.MemberQueryService;
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

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 기본 보안 설정
        http
            .csrf(csrf -> csrf.disable())  // CSRF 보호 비활성화 (REST API에서는 불필요)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // 세션 미사용 (JWT 사용)
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/auth/**", "/oauth2/**").permitAll()  // 인증 관련 엔드포인트는 모두에게 허용
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()  // API 문서 접근 허용
                .anyRequest().authenticated()  // 나머지 요청은 인증 필요
            )
            // OAuth2 로그인 설정
            .oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfo -> userInfo
                    .userService(oAuth2UserService)  // 사용자 정보를 가져오는 서비스 설정
                )
                .successHandler(oAuth2SuccessHandler)  // 로그인 성공 시 처리
                .failureHandler((request, response, exception) -> {
                    // 로그인 실패 시 로그 기록 및 에러 응답
                    log.error("OAuth2 로그인 실패: ", exception);
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "OAuth2 로그인 실패");
                })
            )
            // JWT 인증 필터 추가
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        // CORS 설정
        http
            .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
                @Override
                public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                    CorsConfiguration configuration = new CorsConfiguration();

                    // 프론트엔드 애플리케이션의 도메인 허용
                    configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                    // 허용할 HTTP 메서드 설정
                    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    // 허용할 헤더 설정
                    configuration.setAllowedHeaders(
                        Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "Accept"));
                    // 브라우저에 노출할 헤더 설정
                    configuration.setExposedHeaders(Arrays.asList("Authorization", "Set-Cookie"));
                    // 인증 정보 포함 허용
                    configuration.setAllowCredentials(true);
                    // preflight 요청 캐시 시간
                    configuration.setMaxAge(3600L);

                    return configuration;
                }
            }));

        // JWT 로그인 필터 설정
        JwtLoginFilter jwtLoginFilter = new JwtLoginFilter(
            authenticationManager(authenticationConfiguration),
            jwtProvider,
            authCommandService,
            memberRepository,
            objectMapper
        );
        jwtLoginFilter.setFilterProcessesUrl("/auth/login");  // 로그인 URL 설정
        http.addFilterAt(jwtLoginFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtProvider, memberRepository, objectMapper, memberQueryService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // 비밀번호 암호화를 위한 BCrypt 인코더 사용
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}