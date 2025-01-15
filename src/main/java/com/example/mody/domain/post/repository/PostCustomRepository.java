package com.example.mody.domain.post.repository;

import com.example.mody.domain.bodytype.entity.BodyType;
import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.post.dto.response.PostListResponse;
import com.example.mody.domain.post.dto.response.recode.LikedPostsResponse;
import com.example.mody.domain.post.entity.Post;
import jakarta.persistence.EntityManager;

import java.util.Optional;

public interface PostCustomRepository {

    public PostListResponse getPostList(Optional<Post> cursorPost, Integer size, Member member, Optional<BodyType> bodyType);
    public LikedPostsResponse getLikedPosts(Long cursor, Integer size, Member member);
    public PostListResponse getMyPosts(Long cursor, Integer size, Member member);
}
