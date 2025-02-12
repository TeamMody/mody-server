package com.example.mody.domain.post.service;

import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.post.dto.request.PostCreateRequest;
import com.example.mody.domain.post.dto.request.PostUpdateRequest;
import com.example.mody.domain.post.dto.response.PostResponse;
import com.example.mody.domain.post.entity.Post;

public interface PostCommandService {
	public void createPost(PostCreateRequest postCreateRequest, Member member);

	public void deletePost(Long postId, Member member);

	void togglePostLike(Long myId, Long postId);

	public void reportPost(Member member, Long postId);
	public void updatePost(PostUpdateRequest request, Long postId, Member member);

	void togglePostPublic(Member member, Long postId);
}
