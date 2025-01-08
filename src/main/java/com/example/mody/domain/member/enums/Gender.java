package com.example.mody.domain.member.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "성별")
public enum Gender {
	@Schema(description = "남성")
	MALE,

	@Schema(description = "여성")
	FEMALE
}
