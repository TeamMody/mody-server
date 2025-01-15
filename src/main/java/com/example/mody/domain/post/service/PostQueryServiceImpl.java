package com.example.mody.domain.post.service;

import com.example.mody.domain.bodytype.entity.BodyType;
import com.example.mody.domain.bodytype.service.BodyTypeService;
import com.example.mody.domain.exception.PostException;
import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.post.dto.response.PostListResponse;
import com.example.mody.domain.post.dto.response.PostResponse;
import com.example.mody.domain.post.dto.response.recode.LikedPostsResponse;
import com.example.mody.domain.post.entity.Post;
import com.example.mody.domain.post.entity.mapping.MemberPostLike;
import com.example.mody.domain.post.repository.MemberPostLikeRepository;
import com.example.mody.domain.post.repository.PostRepository;
import com.example.mody.global.common.exception.RestApiException;
import com.example.mody.global.common.exception.code.status.GlobalErrorStatus;
import com.example.mody.global.common.exception.code.status.MemberPostLikeErrorStatus;
import com.example.mody.global.common.exception.code.status.PostErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostQueryServiceImpl implements PostQueryService {
    private final BodyTypeService bodyTypeService;
    private final PostRepository postRepository;
    private final MemberPostLikeRepository memberPostLikeRepository;

    @Override
    public PostListResponse getPosts(Member member, Integer size, Long cursor) {

        if(size<=0){
            throw new RestApiException(GlobalErrorStatus.NEGATIVE_PAGE_SIZE_REQUEST);
        }

        Optional<Post> cursorPost = getCursorPost(cursor);

        if(member != null){
            Optional<BodyType> bodyTypeOptional = bodyTypeService.findLastBodyType(member);
            return postRepository.getPostList(cursorPost, size, member, bodyTypeOptional);
        }

        return postRepository.getPostList(cursorPost, size, member, Optional.empty());
    }

    @Override
    public PostListResponse getLikedPosts(Member member, Integer size, Long cursor) {
        if(size<=0){
            throw new RestApiException(GlobalErrorStatus.NEGATIVE_PAGE_SIZE_REQUEST);
        }
        LikedPostsResponse likedPostsResponse = postRepository.getLikedPosts(cursor, size, member);

        PostResponse postResponse = likedPostsResponse.postResponses().getLast();
        MemberPostLike memberPostLike = findByMemberAndPostId(member, postResponse.getPostId());

        return PostListResponse.from(likedPostsResponse.hasNext(), likedPostsResponse.postResponses(), memberPostLike.getId());
    }

    @Override
    public PostListResponse getMyPosts(Member member, Integer size, Long cursor) {
        if(size<=0){
            throw new RestApiException(GlobalErrorStatus.NEGATIVE_PAGE_SIZE_REQUEST);
        }
        return postRepository.getMyPosts(cursor, size, member);
    }

    public Optional getCursorPost(Long cursor){
        return cursor != null ? Optional.ofNullable(findById(cursor)) : Optional.empty();
    }

    public Post findById(Long id){
        return postRepository.findById(id).orElseThrow(()->
                new PostException(PostErrorStatus.POST_NOT_FOUND));
    }

    public MemberPostLike findByMemberAndPostId(Member member, Long postId){
        return memberPostLikeRepository.findByMemberAndPostId(member, postId).orElseThrow(()
                -> new PostException(MemberPostLikeErrorStatus.LIKE_NOT_FOUND));
    }

}
