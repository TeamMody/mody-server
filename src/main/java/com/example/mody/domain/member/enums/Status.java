package com.example.mody.domain.member.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {

	ACTIVE("활성화"),
	INACTIVE("비활성화"),
	DELETED("삭제")
	;

	private final String description;

}
