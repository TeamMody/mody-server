package com.example.mody.domain.member.service;

import com.example.mody.domain.auth.dto.request.MemberJoinRequest;
import com.example.mody.domain.auth.dto.request.MemberRegistrationRequest;
import com.example.mody.domain.auth.dto.response.LoginResponse;

import com.example.mody.domain.auth.security.CustomUserDetails;
import com.example.mody.domain.member.entity.Member;
import jakarta.servlet.http.HttpServletResponse;

public interface MemberCommandService {
	void completeRegistration(Member member, MemberRegistrationRequest request);

	LoginResponse joinMember(MemberJoinRequest request, HttpServletResponse response);

	void withdrawMember(Long id);
}
