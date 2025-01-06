package com.example.mody.domain.auth.dto.response;

import java.util.Map;

import lombok.Getter;

@Getter
public class KakaoResponse implements OAuth2Response {
	private Map<String, Object> attribute;
	private Map<String, Object> kakaoAccount;
	private Map<String, Object> profile;

	public KakaoResponse(Map<String, Object> attribute) {
		this.attribute = attribute;
		this.kakaoAccount = (Map<String, Object>)attribute.get("kakao_account");
		this.profile = (Map<String, Object>)kakaoAccount.get("profile");
	}

	@Override
	public String getProvider() {
		return "kakao";
	}

	@Override
	public String getProviderId() {
		return attribute.get("id").toString();
	}

	@Override
	public String getName() {
		return profile.get("nickname").toString();
	}
}