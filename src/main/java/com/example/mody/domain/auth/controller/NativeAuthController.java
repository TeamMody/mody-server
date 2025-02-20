package com.example.mody.domain.auth.controller;

import com.example.mody.domain.auth.dto.request.MemberLoginReqeust;
import com.example.mody.domain.auth.dto.response.TokenResponse;
import com.example.mody.domain.auth.service.AuthCommandService;
import com.example.mody.domain.auth.service.NativeAuthCommandService;
import com.example.mody.domain.auth.service.email.EmailService;
import com.example.mody.domain.member.service.MemberCommandService;
import com.example.mody.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth API", description = "인증 관련 API - 회원가입, 로그인, 토큰 재발급, 로그아웃 등의 기능을 제공합니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/native")
public class NativeAuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final AuthCommandService authCommandService;
    private final NativeAuthCommandService nativeAuthCommandService;
    private final MemberCommandService memberCommandService;
    private final EmailService emailService;

    @Operation(summary = "native용 리이슈 API", description = "네이티브에서 사용하는 리이슈 API")
    @PostMapping("/reissue")
    public BaseResponse<TokenResponse> webReissueToken(
            @RequestHeader(value = "refreshToken") String refreshToken
    ) {
        log.info("refreshToken: {}", refreshToken);
        return BaseResponse.onSuccess(authCommandService.nativeReissueToken(refreshToken));
    }

    @Operation(summary = "native용 로그아웃 API", description = "native용 로그아웃을 수행하는 API입니다. 리프레시 토큰을 만료시킵니다.")
    @PostMapping("/logout")
    public BaseResponse<Void> logout(
            @RequestHeader(value = "refreshToken") String refreshToken
    ) {
        authCommandService.logout(refreshToken);

        return BaseResponse.onSuccess(null);
    }

    // todo : 로그인
    @Operation(summary = "native용 로그인 API", description = "네이티브에서 사용하는 로그인 API")
    @PostMapping("/login")
    public BaseResponse<TokenResponse> login(
            @RequestBody MemberLoginReqeust request
    ) {
        return BaseResponse.onSuccess(nativeAuthCommandService.nativeLogin(request));
    }
}
