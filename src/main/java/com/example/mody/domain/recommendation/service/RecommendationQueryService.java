package com.example.mody.domain.recommendation.service;

import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.recommendation.dto.response.RecommendResponses;
import com.example.mody.domain.recommendation.dto.response.CategoryResponse;
import com.example.mody.domain.recommendation.entity.Recommendation;

public interface RecommendationQueryService {

    CategoryResponse getCategories();

    RecommendResponses getRecommendations(Member member, Integer size, Long cursor);

    Recommendation findById(Long recommendationId);
}
