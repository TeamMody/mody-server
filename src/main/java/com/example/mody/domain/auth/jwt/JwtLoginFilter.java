package com.example.mody.domain.auth.jwt;

import com.example.mody.domain.auth.dto.request.MemberLoginReqeust;
import com.example.mody.domain.auth.dto.response.LoginResponse;
import com.example.mody.domain.auth.security.CustomUserDetails;
import com.example.mody.domain.auth.service.AuthCommandService;
import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.member.repository.MemberRepository;
import com.example.mody.global.common.base.BaseResponse;
import com.example.mody.global.common.exception.RestApiException;
import com.example.mody.global.common.exception.code.status.AuthErrorStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;


@RequiredArgsConstructor
public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final AuthCommandService authCommandService;
    private final MemberRepository memberRepository;
    private final ObjectMapper objectMapper;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException{

        //Json형식 데이터 추출
        try {
            MemberLoginReqeust loginReqeust = objectMapper.readValue(request.getInputStream(), MemberLoginReqeust.class);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginReqeust.getEmail(), loginReqeust.getPassword(), null);

            //authenticationManager가 이메일, 비밀번호로 검증을 진행
            return authenticationManager.authenticate(authToken);

        } catch (IOException e) {
            throw new RuntimeException("Failed to parse authentication request body", e);
        }
    }

    //로그인 성공시, JWT토큰 발급
    @Override
    public void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                         Authentication authResult) throws IOException, ServletException {

        CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();

        String username = customUserDetails.getUsername();
        Member member = memberRepository.findByEmail(username)
                .orElseThrow(() -> new RestApiException(AuthErrorStatus.INVALID_ID_TOKEN));

        // Access Token, Refresh Token 발급
        String accessToken = jwtProvider.createAccessToken(member.getId().toString());
        String refreshToken = jwtProvider.createRefreshToken(member.getId().toString());

        // Refresh Token 저장
        authCommandService.saveRefreshToken(member, refreshToken);

        // Refresh Token을 쿠키에 설정
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .maxAge(30)
                .path("/")
                .build();

        // Access Token을 Authorization 헤더에 설정
        response.setHeader("Authorization", "Bearer " + accessToken);
        response.setHeader("Set-Cookie", refreshTokenCookie.toString());

        // 로그인 응답 데이터 설정
        LoginResponse loginResponse = LoginResponse.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .build();

        // 응답 바디 작성
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(BaseResponse.onSuccess(loginResponse)));
    }

    //로그인 실패한 경우 응답처리
    @Override
    public void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                    AuthenticationException failed) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        String errorMessage;
        String errorCode = "AUTH401"; // 기본 에러 코드

        //존재하지 않는 이메일인 경우, 비밀번호가 올라바르지 않은 경우에 따른 예외처리
        if (failed.getCause() instanceof RestApiException) {
            RestApiException restApiException = (RestApiException) failed.getCause();
            errorMessage = restApiException.getErrorCode().getMessage(); //"해당 회원은 존재하지 않습니다."
            errorCode = restApiException.getErrorCode().getCode();
        } else if (failed instanceof BadCredentialsException) {
            errorMessage = "비밀번호가 올바르지 않습니다.";
            errorCode = "AUTH_INVALID_PASSWORD";
        } else {
            errorMessage = "인증에 실패했습니다.";
        }

        // JSON 응답 작성
        BaseResponse<Object> errorResponse = BaseResponse.onFailure(errorCode, errorMessage, null);
        String jsonResponse = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(jsonResponse);
    }

}
