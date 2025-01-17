package com.example.mody.domain.post.service;

import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.post.dto.request.PostCreateRequest;

public interface PostCommandService {
	public void createPost(PostCreateRequest postCreateRequest, Member member);
  	public void deletePost(Long postId);
	void togglePostLike(Long myId, Long postId);
	public void reportPost(Member member, Long postId);
}
