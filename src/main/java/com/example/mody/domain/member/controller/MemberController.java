package com.example.mody.domain.member.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mody.domain.member.dto.MemberRegistrationRequest;
import com.example.mody.domain.member.service.MemberCommandService;
import com.example.mody.global.common.base.BaseResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Member API", description = "회원 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

	private final MemberCommandService memberCommandService;

	@Operation(summary = "회원가입 완료 API", description = "소셜 로그인 후 추가 정보를 입력받아 회원가입을 완료하는 API")
	@PostMapping("/signup")
	public BaseResponse<Void> completeRegistration(
		@Valid @RequestBody MemberRegistrationRequest request) {
		memberCommandService.completeRegistration(request);
		return BaseResponse.onSuccess(null);
	}
}