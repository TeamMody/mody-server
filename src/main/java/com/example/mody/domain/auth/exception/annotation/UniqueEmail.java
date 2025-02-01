package com.example.mody.domain.auth.exception.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.example.mody.domain.auth.exception.validator.ExistsEmailValidator;

import jakarta.validation.Constraint;

@Documented
@Constraint(validatedBy = ExistsEmailValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueEmail {

	String message() default "이미 존재하는 이메일입니다.";

	Class<?>[] groups() default {};

	Class<?>[] payload() default {};
}
