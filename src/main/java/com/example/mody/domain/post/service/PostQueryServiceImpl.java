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
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.example.mody.global.common.exception.code.status.PostErrorStatus.POST_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PostQueryServiceImpl implements PostQueryService {
    private final BodyTypeService bodyTypeService;
    private final PostRepository postRepository;
    private final MemberPostLikeRepository memberPostLikeRepository;
    private final MemberPostLikeRepository postLikeRepository;

    @Override
    @Transactional(readOnly = true)
    public PostListResponse getPosts(Member member, Integer size, Long cursor) {
        if(size<=0){
            throw new RestApiException(GlobalErrorStatus.NEGATIVE_PAGE_SIZE_REQUEST);
        }

        Optional<BodyType> bodyTypeOptional = bodyTypeService.findLastBodyType(member);
        if(bodyTypeOptional.isEmpty()){
            return getRecentPostResponses(member, size, cursor);
        }
        BodyType userBodyType = bodyTypeOptional.get();

        Optional<Post> cursorPost = getCursorPost(cursor);

        return getPostsByBodyType(member, size, cursorPost, userBodyType);
    }

    @Override
    @Transactional(readOnly = true)
    public PostListResponse getLikedPosts(Member member, Integer size, Long cursor) {
        if(size<=0){
            throw new RestApiException(GlobalErrorStatus.NEGATIVE_PAGE_SIZE_REQUEST);
        }
        LikedPostsResponse likedPostsResponse = postRepository.getLikedPosts(cursor, size, member);

        Optional<Long> nextLike = Optional.empty();
        if(likedPostsResponse.hasNext()){
            PostResponse postResponse = likedPostsResponse.postResponses().getLast();
            nextLike = Optional.ofNullable(findByMemberAndPostId(member, postResponse.getPostId()).getId());
        }

        return PostListResponse.of(likedPostsResponse.hasNext(), likedPostsResponse.postResponses(), nextLike.orElse(null));
    }

    @Override
    @Transactional(readOnly = true)
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

    @Override
    public PostResponse getPost(Member member, Long postId){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(POST_NOT_FOUND));

        Optional<MemberPostLike> existingLike = postLikeRepository.findByPostAndMember(post, member);

        PostResponse postResponse = new PostResponse(post.getId(), post.getMember().getId(),post.getMember().getNickname(),post.getMember().equals(member), post.getContent(), post.getIsPublic(), post.getLikeCount(), existingLike.isPresent() ,post.getBodyType().getName(), post.getImages());

        return postResponse;
    }

    private PostListResponse getRecentPostResponses(Member member, Integer size, Long cursor){
        return postRepository.getRecentPosts(cursor, size, member);
    }

    private PostListResponse getPostsByBodyType(
            Member member, Integer size, Optional<Post> cursorPost, BodyType userBodyType){
        // 커서가 존재하지 않거나(== 최초 조회) 커서 post의 바디타입이 유저의 바디타입과 일치하는 경우
        if(cursorPost.isEmpty() ||
                cursorPost.get().getBodyType().equals(userBodyType)){
            return getBodyTypePosts(member, size, cursorPost, userBodyType);
        }
        return getOtherBodyTypePosts(member, size,Optional.empty(), userBodyType);
    }

    private PostListResponse getBodyTypePosts(
            Member member, Integer size, Optional<Post> cursorPost, BodyType userBodyType){
        PostListResponse bodyTypePosts = postRepository.getBodyTypePosts(cursorPost, size, member, userBodyType);

        int insufficientCount = size - bodyTypePosts.getPostResponses().size();
        if (insufficientCount > 0){
            PostListResponse otherBodyTypePosts = getOtherBodyTypePosts(
                    member, insufficientCount,Optional.empty(), userBodyType);
            return PostListResponse.of(bodyTypePosts, otherBodyTypePosts);
        }
        return bodyTypePosts;
    }

    private PostListResponse getOtherBodyTypePosts(
            Member member, Integer size, Optional<Post> cursorPost, BodyType userBodyType){
        return postRepository.getOtherBodyTypePosts(cursorPost, size, member, userBodyType);
    }


}
