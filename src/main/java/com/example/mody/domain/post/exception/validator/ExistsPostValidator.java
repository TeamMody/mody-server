package com.example.mody.domain.post.exception.validator;

import org.springframework.stereotype.Component;

import com.example.mody.domain.post.exception.annotation.ExistsPost;
import com.example.mody.domain.post.repository.PostRepository;
import com.example.mody.global.common.exception.code.status.PostErrorStatus;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ExistsPostValidator implements ConstraintValidator<ExistsPost, Long> {

	private final PostRepository postRepository;

	@Override
	public boolean isValid(Long postId, ConstraintValidatorContext context) {

		if (postId == null || !postRepository.existsById(postId)) {

			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(
				PostErrorStatus.POST_NOT_FOUND.getMessage()
			).addConstraintViolation();

			return false;
		} else {
			return true;
		}
	}
}
