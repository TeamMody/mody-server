package com.example.mody.domain.auth.jwt;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.mody.domain.auth.security.CustomUserDetails;
import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.member.repository.MemberRepository;
import com.example.mody.domain.member.service.MemberQueryService;
import com.example.mody.global.common.exception.RestApiException;
import com.example.mody.global.common.exception.code.status.AuthErrorStatus;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * JWT 토큰을 검증하고 인증 정보를 SecurityContextHolder에 저장하는 필터
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtProvider jwtProvider;
	private final MemberQueryService memberQueryService;
	private final ObjectMapper objectMapper;

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		String uri = request.getRequestURI();
		String contextPath = request.getContextPath();
		if (contextPath != null && !contextPath.isEmpty()) {
			uri = uri.substring(contextPath.length());
		}
		log.info("JwtAuthenticationFilter - Request URI after context removal: {}", uri);
		boolean skip = uri.startsWith("/auth/reissue") ||
			uri.startsWith("/oauth2/") ||
			uri.startsWith("/email/") ||
			uri.startsWith("/swagger-ui/") ||
			uri.startsWith("/v3/api-docs/");

		skip = (!uri.startsWith("/auth/signup/complete")
				&& !uri.startsWith("/auth/logout"))
				&& skip
				&& !uri.startsWith("/auth/native/logout");


		log.info("JwtAuthenticationFilter - shouldNotFilter returns: {}", skip);
		return skip;

	}

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {
		try {
			// 헤더에서 토큰 추출
			String token = resolveToken(request);

			// 만약 토큰이 있다면
			if (token != null) {
				String memberId = jwtProvider.validateTokenAndGetSubject(token);
				Member member = memberQueryService.findMemberById(Long.parseLong(memberId));

				CustomUserDetails customUserDetails = new CustomUserDetails(member);

				Authentication authentication = new UsernamePasswordAuthenticationToken(
					customUserDetails,
					null,
					List.of(new SimpleGrantedAuthority(member.getRole().toString()))
				);

				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
			filterChain.doFilter(request, response);
		} catch (RestApiException e) {
			SecurityContextHolder.clearContext();
			sendErrorResponse(response, e);
		} catch (Exception e) {
			SecurityContextHolder.clearContext();
			sendErrorResponse(response, new RestApiException(AuthErrorStatus.INVALID_ACCESS_TOKEN));
		}
	}

	private String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}

	private void sendErrorResponse(HttpServletResponse response, RestApiException exception) throws IOException {
		response.setStatus(exception.getErrorCode().getHttpStatus().value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");

		Map<String, Object> errorResponse = Map.of(
			"timestamp", java.time.LocalDateTime.now().toString(),
			"code", exception.getErrorCode().getCode(),
			"message", exception.getErrorCode().getMessage()
		);

		response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
	}
}
