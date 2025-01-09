package com.example.mody.domain.member.service;

import com.example.mody.domain.auth.dto.request.MemberJoinRequest;
import com.example.mody.domain.member.converter.MemberConverter;
import org.springframework.security.crypto.password.PasswordEncoder;
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
	private final PasswordEncoder passwordEncoder;

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

	//회원가입
	@Override
	public void joinMember(MemberJoinRequest request) {
		String email = request.getEmail();
		Boolean isExist = memberRepository.existsByEmail(email);

		//이미 존재하는 회원인 경우 예외처리
		if (isExist) {

			throw new MemberException(MemberErrorStatus.EMAIL_ALREADY_EXISTS);
		}

		Member newMember = MemberConverter.toMember(request, email);
		newMember.encodePassword(passwordEncoder.encode(request.getPassword()));
		memberRepository.save(newMember);
	}
}