package com.example.mody.domain.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.mody.domain.auth.dto.request.MemberRegistrationRequest;
import com.example.mody.domain.exception.MemberException;
import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.member.repository.MemberRepository;
import com.example.mody.global.common.exception.code.status.MemberErrorStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberCommandServiceImpl implements MemberCommandService {

	private final MemberRepository memberRepository;

	@Override
	public void completeRegistration(MemberRegistrationRequest request) {
		Member member = memberRepository.findById(request.getMemberId())
			.orElseThrow(() -> new MemberException(MemberErrorStatus.MEMBER_NOT_FOUND));

		member.completeRegistration(
			request.getNickname(),
			request.getBirthDate(),
			request.getGender(),
			request.getHeight(),
			request.getProfileImageUrl()
		);
	}
}