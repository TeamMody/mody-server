package com.example.mody.domain.auth.dto.request;

import com.example.mody.domain.auth.exception.annotation.UniqueEmail;

import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class EmailRequest {
	@Email
	@UniqueEmail
	private String email;
}

