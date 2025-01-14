package com.example.mody.domain.post.service;

import com.example.mody.domain.bodytype.entity.BodyType;
import com.example.mody.domain.bodytype.service.BodyTypeService;
import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.post.dto.response.PostListResponse;
import com.example.mody.domain.post.repository.PostRepository;
import com.example.mody.global.common.exception.RestApiException;
import com.example.mody.global.common.exception.code.status.GlobalErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostQueryServiceImpl implements PostQueryService {
    private final PostRepository postRepository;

    private final BodyTypeService bodyTypeService;


    @Override
    public PostListResponse getPosts(Member member, Integer size, Long cursor) {

        if(size<=0){
            throw new RestApiException(GlobalErrorStatus.NEGATIVE_PAGE_SIZE_REQUEST);
        }

        if(member != null){
            Optional<BodyType> bodyTypeOptional = bodyTypeService.findLastBodyType(member);
            return postRepository.getPostList(cursor, size, member, bodyTypeOptional);
        }

        return postRepository.getPostList(cursor, size, member, Optional.empty());
    }

    @Override
    public PostListResponse getLikedPosts(Member member, Integer size, Long cursor) {
        if(size<=0){
            throw new RestApiException(GlobalErrorStatus.NEGATIVE_PAGE_SIZE_REQUEST);
        }
        return postRepository.getLikedPosts(cursor, size, member);
    }

    @Override
    public PostListResponse getMyPosts(Member member, Integer size, Long cursor) {
        if(size<=0){
            throw new RestApiException(GlobalErrorStatus.NEGATIVE_PAGE_SIZE_REQUEST);
        }
        return postRepository.getMyPosts(cursor, size, member);
    }
}
