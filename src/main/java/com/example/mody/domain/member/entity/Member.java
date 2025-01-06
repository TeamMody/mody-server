package com.example.mody.domain.member.entity;

import java.time.LocalDate;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.example.mody.domain.member.enums.Gender;
import com.example.mody.domain.member.enums.Role;
import com.example.mody.domain.member.enums.Status;
import com.example.mody.global.common.base.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Table(name = "member")
public class Member extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long id;

	private String providerId;  // OAuth2 제공자의 고유 ID

	private String provider;    // OAuth2 제공자 (kakao, google 등)

	private String email;

	private String nickname;

	private String password;

	private String profileImageUrl;

	private LocalDate birthDate;

	@Enumerated(EnumType.STRING)
	private Gender gender;

	private Integer height;

	@Enumerated(EnumType.STRING)
	private Status status;

	@Enumerated(EnumType.STRING)
	private Role role;

	@Builder.Default
	private boolean isRegistrationCompleted = false;  // 회원가입 완료 여부

	public void completeRegistration(String nickname, LocalDate birthDate, Gender gender, Integer height
		, String profileImageUrl) {
		this.nickname = nickname;
		this.birthDate = birthDate;
		this.gender = gender;
		this.height = height;
		this.profileImageUrl = profileImageUrl;
		this.isRegistrationCompleted = true;
	}

}
