package com.example.mody.domain.member.controller;

import com.example.mody.domain.member.dto.request.MemberEditRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.example.mody.domain.auth.security.CustomUserDetails;
import com.example.mody.domain.member.dto.response.MemberProfileResponse;
import com.example.mody.domain.member.service.MemberCommandService;
import com.example.mody.domain.member.service.MemberQueryService;
import com.example.mody.global.common.base.BaseResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

	private final MemberQueryService memberQueryService;
	private final MemberCommandService memberCommandService;

	@GetMapping("/me")
	@Operation(summary = "자신의 프로필 조회 API", description = "요청 클라이언트의 정보를 반환하는 API")
	@ApiResponses({
		@ApiResponse(responseCode = "COMMON200", description = "프로필 조회 성공"),
	})
	public BaseResponse<MemberProfileResponse> getMyProfile(
		@AuthenticationPrincipal CustomUserDetails customUserDetails) {
		MemberProfileResponse response = memberQueryService.getMyProfile(customUserDetails.getMember());
		return BaseResponse.onSuccess(response);
	}

	/**
	 * 회원탈퇴 API (soft delete)
	 */
	@PostMapping("/withdraw")
	@Operation(summary = "회원 탈퇴 API", description = "회원 탈퇴하는 API")
	@ApiResponses({
			@ApiResponse(responseCode = "COMMON200", description = "회원 탈퇴 성공"),
	})
	public BaseResponse<Void> withdraw(@AuthenticationPrincipal CustomUserDetails userDetails) {
		// 현재 로그인된 회원의 id로 탈퇴 처리
		memberCommandService.withdrawMember(userDetails.getMember().getId());
		return BaseResponse.onSuccess(null);
	}

	@PatchMapping("/edit")
	@Operation(summary = "회원정보 수정 API", description = "회원 정보를 수정하는 API")
	@ApiResponses({
			@ApiResponse(responseCode = "COMMON200", description = "회원 탈퇴 성공"),
	})
	public BaseResponse<Void> edit(
			@AuthenticationPrincipal CustomUserDetails customUserDetails,
			@Valid @RequestBody MemberEditRequest request) {
		memberCommandService.editProfile(request, customUserDetails.getMember());
		return BaseResponse.onSuccess(null);
	}
}
