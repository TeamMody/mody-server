package com.example.mody.domain.member.service;

import com.example.mody.domain.auth.dto.request.MemberJoinRequest;
import com.example.mody.domain.auth.dto.request.MemberRegistrationRequest;

public interface MemberCommandService {
	void completeRegistration(MemberRegistrationRequest request);

	void joinMember(MemberJoinRequest request);
}
