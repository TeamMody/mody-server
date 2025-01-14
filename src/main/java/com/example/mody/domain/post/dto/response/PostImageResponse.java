package com.example.mody.domain.post.dto.response;

import com.example.mody.domain.post.entity.PostImage;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostImageResponse {
    private String s3Url;
    public static PostImageResponse from(PostImage postImage){
        return new PostImageResponse(postImage.getUrl());
    }
}
