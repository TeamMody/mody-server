package com.example.mody.domain.post.entity.mapping;

import jakarta.persistence.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.post.entity.Post;
import com.example.mody.global.common.base.BaseEntity;

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
@Table(name = "member_post_like", indexes = {
		@Index(name = "idx_member_post",
				columnList = "member_id, post_id")
})
public class MemberPostLike extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_post_like_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id")
	private Post post;

	public static MemberPostLike createMemberPostLike(Member member, Post post) {
		return MemberPostLike.builder()
			.member(member)
			.post(post)
			.build();
	}

}
