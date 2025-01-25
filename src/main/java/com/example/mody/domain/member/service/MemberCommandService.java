package com.example.mody.domain.member.service;

import com.example.mody.domain.auth.dto.request.MemberJoinRequest;
import com.example.mody.domain.auth.dto.request.MemberRegistrationRequest;
import com.example.mody.domain.auth.dto.response.LoginResponse;
import jakarta.servlet.http.HttpServletResponse;

public interface MemberCommandService {
	void completeRegistration(Long memberId, MemberRegistrationRequest request);

	LoginResponse joinMember(MemberJoinRequest request, HttpServletResponse response);
}
