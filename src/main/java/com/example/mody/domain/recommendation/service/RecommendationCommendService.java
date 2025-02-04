package com.example.mody.domain.recommendation.service;

import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.recommendation.dto.request.RecommendRequest;
import com.example.mody.domain.recommendation.dto.response.RecommendResponse;
import com.example.mody.domain.recommendation.dto.response.RecommendLikeResponse;

public interface RecommendationCommendService {

    RecommendResponse recommendStyle(Member member, RecommendRequest request);

    RecommendResponse recommendFashionItem(Member member, RecommendRequest request);

    RecommendLikeResponse toggleLike(Long recommendationId, Member member);
}
