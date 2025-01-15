package com.example.mody.domain.post.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.post.entity.Post;
import com.example.mody.domain.post.entity.mapping.MemberPostLike;

public interface MemberPostLikeRepository extends JpaRepository<MemberPostLike, Long> {

	Optional<MemberPostLike> findByPostAndMember(Post post, Member member);
	
}
