package com.example.mody.domain.post.service;

import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.post.dto.response.PostListResponse;

public interface PostQueryService {
    public PostListResponse getPosts(Member member, Integer size, Long cursor);
    public PostListResponse getLikedPosts(Member member, Integer size, Long cursor);
    public PostListResponse getMyPosts(Member member, Integer size, Long cursor);
}
