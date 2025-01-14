package com.example.mody.domain.post.dto.response;

import com.example.mody.global.dto.response.CursorPagination;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostListResponse {
    private List<PostResponse> postResponses;
    private CursorPagination cursorPagination;

    public static PostListResponse from(Boolean hasNext, List<PostResponse> postResponses){
        return new PostListResponse(postResponses, new CursorPagination(hasNext, postResponses.getLast().getPostId()));
    }
}
