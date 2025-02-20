package com.example.mody.domain.member.service;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.mody.domain.auth.dto.request.MemberJoinRequest;
import com.example.mody.domain.auth.dto.request.MemberRegistrationRequest;
import com.example.mody.domain.auth.dto.response.LoginResponse;
import com.example.mody.domain.auth.service.AuthCommandService;
import com.example.mody.domain.exception.MemberException;
import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.member.enums.LoginType;
import com.example.mody.domain.member.enums.Role;
import com.example.mody.domain.member.enums.Status;
import com.example.mody.domain.member.repository.MemberRepository;
import com.example.mody.global.common.exception.code.status.MemberErrorStatus;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberCommandServiceImpl implements MemberCommandService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthCommandService authCommandService;
	private final MemberQueryService memberQueryService;

	@Override
	public void completeRegistration(Member member, MemberRegistrationRequest request) {
		Member unregisteredMember = memberQueryService.findMemberById(member.getId()); // 영속성 컨텍스트가 관리하도록
		unregisteredMember.completeRegistration(
			request.getNickname(),
			request.getBirthDate(),
			request.getGender(),
			request.getHeight(),
			request.getProfileImageUrl()
		);
	}

	//회원가입
	@Override
	public LoginResponse joinMember(MemberJoinRequest request, HttpServletResponse response) {
		String email = request.getEmail();
		Boolean isExist = memberRepository.existsByEmail(email);

		//이미 존재하는 회원인 경우 예외처리
		if (isExist) {
			throw new MemberException(MemberErrorStatus.EMAIL_ALREADY_EXISTS);
		}

		//회원 저장
		Member newMember = Member.builder()
			.email(email)
			.status(Status.ACTIVE)
			.reportCount(0)
			.role(Role.ROLE_USER)
			.loginType(LoginType.GENERAL)
			.isRegistrationCompleted(false)
			.build();

		newMember.setEncodedPassword(passwordEncoder.encode(request.getPassword()));
		memberRepository.save(newMember);

		//자동 로그인 처리
		String newAccessToken = authCommandService.processLoginSuccess(newMember, response);

		return LoginResponse.of(
			newMember.getId(),
			newMember.getNickname(),
			true,
			newMember.isRegistrationCompleted(),
			newAccessToken);
	}

	@Override
	public void withdrawMember(Long memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberException(MemberErrorStatus.MEMBER_NOT_FOUND));

		// 회원의 상태를 '삭제'로 변경 (soft delete)
		member.softDelete();
		// 별도의 memberRepository.save(member) 호출은 @Transactional 및 변경 감지로 반영됩니다.
	}
}