package com.example.mody.domain.post.dto.response;

import com.example.mody.domain.bodytype.entity.BodyType;
import com.example.mody.domain.post.entity.PostImage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PostResponse {

    private Long postId;
    private String nickName;
    private String content;
    private Boolean isPublic;
    private Integer likeCount;
    private Boolean isLiked;
    private String bodyType;
    private List<PostImageResponse> files;

    public PostResponse(Long postId,
                        String nickName ,
                        String content,
                        Boolean isPublic,
                        Integer likeCount,
                        Boolean isLiked,
                        String bodyType,
                        List<PostImage> files) {
        this.postId = postId;
        this.nickName = nickName;
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
