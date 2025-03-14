package com.example.mody.domain.post.dto.response;

import com.example.mody.domain.post.entity.PostImage;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PostResponse {

    private Long postId;
    private Long writerId;
    private String writerNickName;
    private Boolean isMine;
    private String content;
    private Boolean isPublic;
    private Integer likeCount;
    private Boolean isLiked;
    private String bodyType;
    private List<PostImageResponse> files;

    public PostResponse(Long postId,
                        Long writerId,
                        String nickName,
                        Boolean isMine,
                        String content,
                        Boolean isPublic,
                        Integer likeCount,
                        Boolean isLiked,
                        String bodyType,
                        List<PostImage> files) {
        this.postId = postId;
        this.writerId = writerId;
        this.writerNickName = nickName;
        this.isMine = isMine;
        this.content = content;
        this.isPublic = isPublic;
        this.likeCount = likeCount;
        this.isLiked = isLiked;
        this.bodyType = bodyType;
        this.files = files.stream()
                .map(PostImageResponse::from)
                .toList();
    }
}
