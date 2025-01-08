package com.example.mody.domain.auth.security;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.mody.domain.auth.dto.response.KakaoResponse;
import com.example.mody.domain.auth.dto.response.OAuth2Response;
import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.member.enums.Role;
import com.example.mody.domain.member.enums.Status;
import com.example.mody.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

	private final MemberRepository memberRepository;

	/**
	 * OAuth2UserRequest를 통해 OAuth2User를 데이터베이스에서 로드하고 회원가입 및 로그인 처리
	 * @param userRequest
	 * @return
	 * @throws OAuth2AuthenticationException
	 */
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);
		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		final OAuth2Response oAuth2Response;

		// 카카오 로그인
		if (registrationId.equals("kakao")) {
			oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
		} else {
			return null;
		}

		// 회원 정보를 조회.
		// 만약 회원이 없다면 회원을 저장함.
		// 그 다음 AuthController의 회원가입 API를 통해 회원가입을 완료해야 함.
		Member member = memberRepository.findByProviderId(oAuth2Response.getProviderId())
			.orElseGet(() -> saveMember(oAuth2Response));

		return new CustomOAuth2User(oAuth2Response, oAuth2User.getAttributes(), member.isRegistrationCompleted());
	}

	private Member saveMember(OAuth2Response oAuth2Response) {
		Member member = Member.builder()
			.providerId(oAuth2Response.getProviderId())
			.provider(oAuth2Response.getProvider())
			.nickname(oAuth2Response.getName())
			.status(Status.ACTIVE)
			.role(Role.ROLE_USER)
			.isRegistrationCompleted(false)  // 최초 가입 시 미완료 상태로 설정
			.build();

		return memberRepository.save(member);
	}
}