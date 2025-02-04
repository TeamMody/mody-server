package com.example.mody.domain.recommendation.repository;

import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.recommendation.dto.response.RecommendResponses;

public interface RecommendCustomRepository {

    RecommendResponses findMyRecommendations(Member member, Integer size, Long Cursor);
}
