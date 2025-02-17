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

    public static PostListResponse of(Boolean hasNext, List<PostResponse> postResponses){
        Long cursor = hasNext ? postResponses.getLast().getPostId() : null;
        return new PostListResponse(postResponses, new CursorPagination(hasNext, cursor));
    }

    /**
     * 내가 좋아요 누른 게시글 조회 시 커서를 좋아요 id로 사용함. 따라서 아래와 같이 메서드를 오버로드하여 따로 사용
     * @param hasNext 다음 페이지 유무
     * @param postResponses 반환할 데이터
     * @param cursor 반환하는 게시물 중 마지막 게시물과 클라이언트에 대한 likeId
     * @return
     */
    public static PostListResponse of(Boolean hasNext, List<PostResponse> postResponses, Long cursor){
        Long newCursor = hasNext ? cursor : null;
        return new PostResponses(postResponses, new CursorPagination(hasNext, newCursor));
    }

    public static PostResponses of(PostResponses firstPosts, PostResponses secondPosts){
        List<PostResponse> newPostResponses = new ArrayList<>(
                firstPosts.getPostResponses().size() + secondPosts.getPostResponses().size());
        newPostResponses.addAll(firstPosts.getPostResponses());
        newPostResponses.addAll(secondPosts.getPostResponses());
        return new PostResponses(newPostResponses, secondPosts.cursorPagination);
    }
}
