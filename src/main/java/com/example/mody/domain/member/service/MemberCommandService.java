package com.example.mody.domain.member.service;

import com.example.mody.domain.member.dto.MemberRegistrationRequest;

public interface MemberCommandService {
	void completeRegistration(MemberRegistrationRequest request);
}
