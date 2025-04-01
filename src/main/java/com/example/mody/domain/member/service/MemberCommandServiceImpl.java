package com.example.mody.domain.member.service;

import com.example.mody.domain.image.service.S3Service;
import com.example.mody.domain.member.dto.request.MemberEditRequest;
import com.example.mody.global.common.exception.RestApiException;
import com.example.mody.global.common.exception.code.status.S3ErrorStatus;
import org.springframework.http.HttpMethod;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberCommandServiceImpl implements MemberCommandService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthCommandService authCommandService;
	private final MemberQueryService memberQueryService;
	private final S3Service s3Service;
	private final RestTemplate restTemplate;

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

	// 회원정보 수정
	@Override
	public void editProfile(MemberEditRequest request, Member member) {
		// 1. request에서 넘어온 프로필 사진과 기존 프로필 사진을 비교해서 다르면 기존 사진을 삭제
		String originProfileImageUrl = member.getProfileImageUrl();
		String newProfileImageUrl = request.getProfileImageUrl();

		if (originProfileImageUrl != null && isProfileImageUpdated(newProfileImageUrl, originProfileImageUrl)) {
			s3Service.deleteProfileImage(originProfileImageUrl);
		}

		validateS3Url(newProfileImageUrl);

		// 2. 프로필 업데이트
		memberQueryService.findMemberById(member.getId()).completeRegistration(
				request.getNickname(),
				request.getBirthDate(),
				request.getGender(),
				request.getHeight(),
				request.getProfileImageUrl()
		);
	}

	private static boolean isProfileImageUpdated(String newProfileImageUrl, String originProfileImageUrl) {
		return !newProfileImageUrl.equals(originProfileImageUrl);
	}

	private void validateS3Url(String s3Url) {
		try {
			// S3 url에 GET 요청을 보내서 유효한지 확인
			// build(true) 사용 -> URL 인코딩된 부분을 그대로 유지
			URI uri = UriComponentsBuilder.fromHttpUrl(s3Url).build(true).toUri();
			restTemplate.exchange(uri, HttpMethod.GET, null, Void.class);
		} catch (HttpClientErrorException e) {
			throw new RestApiException(S3ErrorStatus.OBJECT_NOT_FOUND);
		}
	}
}