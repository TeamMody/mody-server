package com.example.mody.domain.auth.service;

import com.example.mody.domain.auth.dto.TokenDto;
import com.example.mody.domain.auth.dto.request.MemberJoinRequest;
import com.example.mody.domain.member.entity.Member;

import jakarta.servlet.http.HttpServletResponse;

public interface AuthCommandService {

	String reissueToken(String oldRefreshToken, HttpServletResponse response);

	void saveRefreshToken(Member member, String refreshToken);

	void logout(String refreshToken);

	String processLoginSuccess(Member member, HttpServletResponse response);
}
