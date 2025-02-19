package com.example.mody.domain.post.repository;

import com.example.mody.domain.bodytype.entity.BodyType;
import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.post.dto.response.PostResponses;
import com.example.mody.domain.post.dto.response.recode.LikedPostsResponse;
import com.example.mody.domain.post.entity.Post;

import java.util.Optional;

public interface PostCustomRepository {

    public PostResponses getBodyTypePosts(Optional<Post> cursorPost, Integer size, Member member, BodyType bodyType);
    public PostResponses getOtherBodyTypePosts(Optional<Post> cursorPost, Integer size, Member member, BodyType bodyType);
    public LikedPostsResponse getLikedPosts(Long cursor, Integer size, Member member);
    public PostResponses getMyPosts(Long cursor, Integer size, Member member);
    public PostResponses getRecentPosts(Long cursor, Integer size, Member member);

}
