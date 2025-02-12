package com.example.mody.domain.auth.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.example.mody.domain.auth.dto.response.OAuth2Response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User {

	private final OAuth2Response oAuth2Response;
	private final Map<String, Object> attributes;
	private final boolean isRegistrationCompleted;

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new GrantedAuthority() {
			@Override
			public String getAuthority() {
				return "ROLE_USER";
			}
		});
		return authorities;
	}

	@Override
	public String getName() {
		return oAuth2Response.getName();
	}
}