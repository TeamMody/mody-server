package com.example.mody.domain.auth.exception.validator;

import org.springframework.stereotype.Component;

import com.example.mody.domain.auth.exception.annotation.UniqueEmail;
import com.example.mody.domain.member.repository.MemberRepository;
import com.example.mody.global.common.exception.code.status.MemberErrorStatus;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ExistsEmailValidator implements ConstraintValidator<UniqueEmail, String> {

	private final MemberRepository memberRepository;

	@Override
	public void initialize(UniqueEmail constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);
	}

	@Override
	public boolean isValid(String email, ConstraintValidatorContext context) {

		if (memberRepository.existsByEmail(email)) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(MemberErrorStatus.EMAIL_ALREADY_EXISTS.getMessage())
				.addConstraintViolation();
			return false;
		}

		return true;

	}

}
