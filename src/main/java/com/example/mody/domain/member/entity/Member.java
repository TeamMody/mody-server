package com.example.mody.domain.member.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.example.mody.domain.fashionItem.entity.FashionItem;
import com.example.mody.domain.style.entity.Style;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.example.mody.domain.bodytype.entity.mapping.MemberBodyType;
import com.example.mody.domain.member.enums.Gender;
import com.example.mody.domain.member.enums.LoginType;
import com.example.mody.domain.member.enums.Role;
import com.example.mody.domain.member.enums.Status;
import com.example.mody.domain.post.entity.mapping.MemberPostLike;
import com.example.mody.global.common.base.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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

	@OneToMany(mappedBy = "member",
		cascade = CascadeType.ALL,
		orphanRemoval = true,
		fetch = FetchType.LAZY)
	private List<MemberPostLike> likes = new ArrayList<>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.ALL)
	private List<MemberBodyType> memberBodyType = new ArrayList<>();

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Style> styles = new ArrayList<>();

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<FashionItem> FashionItems = new ArrayList<>();

	private String providerId;  // OAuth2 제공자의 고유 ID

	private String provider;    // OAuth2 제공자 (kakao, google 등)

	@Column(unique = true)
	private String email;

	private String nickname;

	private String password;

	private String profileImageUrl;

	private LocalDate birthDate;

	@Builder.Default
	@Column(nullable = false)
	private Integer likeCount = 0;

	@Enumerated(EnumType.STRING)
	private Gender gender;

	private Integer height;

	@Enumerated(EnumType.STRING)
	private Status status;

	@Column(nullable = false)
	@Builder.Default
	private Integer reportCount = 0;

	@Enumerated(EnumType.STRING)
	private Role role;

	@Enumerated(EnumType.STRING)
	private LoginType loginType;

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

	public void setEncodedPassword(String password) {
		this.password = password;
	}

	public void increaseReportCount() {
		this.reportCount++;
	}

	public void increaseLikeCount() {
		this.likeCount++;
	}

	public void decreaseLikeCount() {
		this.likeCount--;
	}

	/**
	 * 게시글 삭제 등으로 좋아요 수를 줄이는 경우
	 * @param count
	 */
	public void decreaseLikeCount(int count){
		this.likeCount -= count;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Member that)) {
			return false;
		}
		return Objects.equals(that.getId(), this.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
