package com.example.mody.domain.post.dto.response.recode;

import com.example.mody.domain.post.dto.response.PostResponse;

import java.util.List;

public record LikedPostsResponse(Boolean hasNext, List<PostResponse> postResponses) {
}
