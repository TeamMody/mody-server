package com.example.mody.domain.recommendation.dto.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.example.mody.domain.recommendation.dto.response.QRecommendResponse is a Querydsl Projection type for RecommendResponse
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QRecommendResponse extends ConstructorExpression<RecommendResponse> {

    private static final long serialVersionUID = 1780340304L;

    public QRecommendResponse(com.querydsl.core.types.Expression<Long> memberId, com.querydsl.core.types.Expression<String> nickname, com.querydsl.core.types.Expression<Long> recommendationId, com.querydsl.core.types.Expression<com.example.mody.domain.recommendation.enums.RecommendType> recommendType, com.querydsl.core.types.Expression<Boolean> isLiked, com.querydsl.core.types.Expression<String> title, com.querydsl.core.types.Expression<String> content, com.querydsl.core.types.Expression<String> imageUrl) {
        super(RecommendResponse.class, new Class<?>[]{long.class, String.class, long.class, com.example.mody.domain.recommendation.enums.RecommendType.class, boolean.class, String.class, String.class, String.class}, memberId, nickname, recommendationId, recommendType, isLiked, title, content, imageUrl);
    }

}

