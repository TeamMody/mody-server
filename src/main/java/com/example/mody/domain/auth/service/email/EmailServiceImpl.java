package com.example.mody.domain.auth.service.email;

import java.time.Duration;
import java.util.Random;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.example.mody.global.common.exception.RestApiException;
import com.example.mody.global.common.exception.code.status.GlobalErrorStatus;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
	private static final long VERIFICATION_TIME_LIMIT = 5L;
	private final JavaMailSender emailSender;
	private final StringRedisTemplate redisTemplate;
	private final SpringTemplateEngine templateEngine;

	public void sendVerificationEmail(String email) {
		try {
			String verificationCode = generateVerificationCode();

			// 템플릿에 전달할 데이터 설정
			Context context = new Context();
			context.setVariable("name", email.split("@")[0]); // 이메일 앞부분을 이름으로 사용
			context.setVariable("verificationCode", verificationCode);

			// 템플릿을 이용해 HTML 생성
			String htmlContent = templateEngine.process("mail/verification-email", context);

			// 이메일 메시지 생성
			MimeMessage message = emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

			helper.setTo(email);
			helper.setSubject("MODY 이메일 인증");
			helper.setText(htmlContent, true); // HTML 사용을 위해 true로 설정

			// 이메일 발송
			emailSender.send(message);

			// Redis에 인증번호 저장
			redisTemplate.opsForValue()
				.set(email, verificationCode, Duration.ofMinutes(VERIFICATION_TIME_LIMIT));

		} catch (MessagingException e) {
			throw new RestApiException(GlobalErrorStatus._INTERNAL_SERVER_ERROR);
		}
	}

	public boolean verifyEmail(String email, String code) {
		String storedCode = redisTemplate.opsForValue().get(email);
		return code.equals(storedCode);
	}

	private String generateVerificationCode() {
		Random random = new Random();
		return String.format("%06d", random.nextInt(1000000));
	}
}
