package com.example.mody.domain.member.controller;

import com.example.mody.domain.auth.security.CustomUserDetails;
import com.example.mody.domain.bodytype.service.BodyTypeService;
import com.example.mody.domain.member.dto.response.MemberProfileResponse;
import com.example.mody.domain.member.service.MemberQueryService;
import com.example.mody.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberQueryService memberQueryService;

    @GetMapping("/me")
    @Operation(summary = "자신의 프로필 조회 API", description = "요청 클라이언트의 정보를 반환하는 API")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON200", description = "프로필 조회 성공"),
    })
    public BaseResponse<MemberProfileResponse> getMyProfile(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        MemberProfileResponse response = memberQueryService.getMyProfile(customUserDetails.getMember());
        return BaseResponse.onSuccess(response);
    }

}
