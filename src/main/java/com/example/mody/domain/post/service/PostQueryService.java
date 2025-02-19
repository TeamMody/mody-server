package com.example.mody.domain.post.service;

import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.post.dto.response.PostResponses;
import com.example.mody.domain.post.dto.response.PostResponse;

public interface PostQueryService {
    public PostResponses getPosts(Member member, Integer size, Long cursor);
    public PostResponses getLikedPosts(Member member, Integer size, Long cursor);
    public PostResponses getMyPosts(Member member, Integer size, Long cursor);
    public PostResponse getPost(Member member, Long postId);
}
