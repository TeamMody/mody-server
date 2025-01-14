package com.example.mody.domain.post.repository;

import com.example.mody.domain.bodytype.entity.BodyType;
import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.post.dto.response.PostListResponse;
import jakarta.persistence.EntityManager;

import java.util.Optional;

public interface PostCustomRepository {

    public PostListResponse getPostList(Long cursor, Integer size, Member member, Optional<BodyType> bodyType);
}
