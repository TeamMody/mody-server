package com.example.mody.domain.post.service;

import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.post.dto.request.PostCreateRequest;

public interface PostCommandService {

	public void createPost(PostCreateRequest postCreateRequest, Member member);

	void togglePostLike(Long myId, Long postId);

}
