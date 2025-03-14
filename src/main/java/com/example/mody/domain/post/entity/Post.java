package com.example.mody.domain.post.entity;

import static com.example.mody.domain.post.constant.PostConstant.*;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import com.example.mody.domain.bodytype.entity.BodyType;
import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.post.entity.mapping.MemberPostLike;
import com.example.mody.global.common.base.BaseEntity;

@Entity(name = "post")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "post", indexes = {
		@Index(name = "idx_bodytype_post",
				columnList = "body_type_id, post_id"),
		@Index(name = "idx_member_post",
				columnList = "member_id, post_id")
})
@DynamicUpdate
public class Post extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_id")
	private Long id;

	@OneToMany(mappedBy = "post",
		cascade = CascadeType.ALL,
		orphanRemoval = true,
		fetch = FetchType.LAZY)
	private List<PostImage> images = new ArrayList<>();

	@OneToMany(mappedBy = "post",
		cascade = CascadeType.ALL,
		orphanRemoval = true)
	private List<MemberPostLike> likes = new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "body_type_id", nullable = false)
	private BodyType bodyType;

	@Column(length = POST_CONTENT_LIMIT)
	private String content;

	@Column(nullable = false)
	private Integer likeCount = 0;

	@Column(nullable = false)
	private Boolean isPublic;

	@Column(nullable = false)
	private Integer reportCount = 0;

	@Builder
	public Post(Member member, BodyType bodyType, String content, Boolean isPublic) {
		this.member = member;
		this.bodyType = bodyType;
		this.content = content;
		this.isPublic = isPublic;
		this.likeCount = 0;
		this.reportCount = 0;
		this.images = new ArrayList<>();
		this.likes = new ArrayList<>();
	}

	public void decreaseLikeCount() {
		this.likeCount--;
	}

	public void increaseLikeCount() {
		this.likeCount++;
	}

	public void increaseReportCount() {
		this.reportCount++;
	}

	public void updatePost(String content, boolean isPublic) {
		this.content = content;
		this.isPublic = isPublic;
	}

	public void toggleIsPublic() {
		this.isPublic = !isPublic;
	}
}
