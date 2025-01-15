package com.example.mody.domain.member.exception.validator;

import org.springframework.stereotype.Component;

import com.example.mody.domain.member.exception.annotation.ExistsMember;
import com.example.mody.domain.member.repository.MemberRepository;
import com.example.mody.global.common.exception.code.status.MemberErrorStatus;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ExistsMemberValidator implements ConstraintValidator<ExistsMember, Long> {

	private final MemberRepository memberRepository;

	@Override
	public void initialize(ExistsMember constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);
	}

	@Override
	public boolean isValid(Long memberId, ConstraintValidatorContext context) {

		if (memberId == null || !memberRepository.existsById(memberId)) {

			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(
				MemberErrorStatus.MEMBER_NOT_FOUND.getMessage()
			).addConstraintViolation();

			return false;
		} else {
			return true;
		}
	}
}
