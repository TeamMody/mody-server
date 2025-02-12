package com.example.mody.domain.auth.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.mody.domain.exception.MemberException;
import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.member.repository.MemberRepository;
import com.example.mody.global.common.exception.code.status.MemberErrorStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final MemberRepository memberRepository;

	//데이터베이스에서 사용자의 정보를 조회
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Member member = memberRepository.findByEmail(username)
			.orElseThrow(() -> new MemberException(MemberErrorStatus.MEMBER_NOT_FOUND));

		// 소셜 로그인 사용자인지 확인
		if (member.getProvider() != null) {
			throw new MemberException(MemberErrorStatus.INVALID_LOGIN_TYPE);
		}

		return new CustomUserDetails(member);
	}
}
