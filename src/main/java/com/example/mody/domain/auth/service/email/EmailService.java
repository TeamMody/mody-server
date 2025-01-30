package com.example.mody.domain.auth.service.email;

public interface EmailService {

	void sendVerificationEmail(String email);

	boolean verifyEmail(String email, String code);
}
