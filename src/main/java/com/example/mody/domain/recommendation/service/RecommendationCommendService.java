package com.example.mody.domain.recommendation.service;

import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.recommendation.dto.request.RecommendRequest;
import com.example.mody.domain.recommendation.dto.request.WeatherRecommendRequest;
import com.example.mody.domain.recommendation.dto.response.RecommendResponse;
import com.example.mody.domain.recommendation.dto.response.RecommendLikeResponse;
import com.example.mody.domain.recommendation.dto.response.analysis.WeatherStyleAnalysisResponse;

public interface RecommendationCommendService {

    RecommendResponse recommendStyle(Member member, RecommendRequest request);

    RecommendResponse recommendFashionItem(Member member, RecommendRequest request);

    RecommendResponse recommendWeatherStyle(Member member, WeatherRecommendRequest request);

    RecommendLikeResponse toggleLike(Long recommendationId, Member member);
}
