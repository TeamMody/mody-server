package com.example.mody.domain.member.service;

import com.example.mody.domain.auth.dto.request.MemberRegistrationRequest;

public interface MemberCommandService {
	void completeRegistration(MemberRegistrationRequest request);
}
