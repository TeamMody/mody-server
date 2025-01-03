package com.example.mody.domain.member.web.controller;

import com.example.mody.domain.member.converter.MemberConverter;
import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.member.service.MemberCommandService;
import com.example.mody.domain.member.web.dto.MemberRequestDTO;
import com.example.mody.domain.member.web.dto.MemberResponseDTO;
import com.example.mody.global.common.base.BaseResponse;
import com.example.mody.global.config.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class MemberRestController {

    private final MemberCommandService memberCommandService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/sign-up")
    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    public BaseResponse<MemberResponseDTO.JoinResultDto> join(@RequestBody @Valid MemberRequestDTO.JoinDto request) {
        Member member = memberCommandService.joinMember(request);
        return BaseResponse.onSuccess(MemberConverter.toJoinResultDto(member));
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "사용자 로그인을 처리하고 JWT 토큰을 반환합니다.")
    public BaseResponse<String> login(@RequestBody @Valid MemberRequestDTO.LoginDto request) {
        Member member = memberCommandService.authenticate(request.getEmail(), request.getPassword()); // 인증 로직 구현
        String token = jwtTokenProvider.createToken(member.getEmail(), member.getRole().name().replace("ROLE_", ""));
        return BaseResponse.onSuccess(token);
    }
}
