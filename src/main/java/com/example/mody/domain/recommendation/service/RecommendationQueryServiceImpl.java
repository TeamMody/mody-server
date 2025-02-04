package com.example.mody.domain.recommendation.service;

import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.recommendation.dto.response.CategoryResponse;
import com.example.mody.domain.recommendation.dto.response.RecommendResponses;
import com.example.mody.domain.recommendation.entity.Recommendation;
import com.example.mody.domain.recommendation.exception.RecommendationException;
import com.example.mody.domain.recommendation.repository.category.AppealCategoryRepository;
import com.example.mody.domain.recommendation.repository.RecommendationRepository;
import com.example.mody.domain.recommendation.repository.category.StyleCategoryRepository;
import com.example.mody.global.common.exception.code.status.RecommendationErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class RecommendationQueryServiceImpl implements RecommendationQueryService{

    private final StyleCategoryRepository styleCategoryRepository;
    private final AppealCategoryRepository appealCategoryRepository;
    private final RecommendationRepository recommendationRepository;

    @Override
    public CategoryResponse getCategories() {
        //styleCategories 조회 및 이름 리스트로 반환
        List<String> styleCategories = styleCategoryRepository.findAll()
                .stream()
                .map(styleCategory -> styleCategory.getName())
                .collect(Collectors.toList());

        if (styleCategories.isEmpty()) {
            throw new RecommendationException(RecommendationErrorStatus.STYLE_CATEGORY_EMPTY);
        }

        List<String> appealCategories = appealCategoryRepository.findAll()
                .stream()
                .map(appealCategory -> appealCategory.getName())
                .collect(Collectors.toList());

        if (appealCategories.isEmpty()) {
            throw new RecommendationException(RecommendationErrorStatus.APPEAL_CATEGORY_EMPTY);
        }

        return new CategoryResponse(styleCategories, appealCategories);
    }

    @Override
    public RecommendResponses getRecommendations(Member member, Integer size, Long cursor) {
        RecommendResponses responses = recommendationRepository.findMyRecommendations(member, size, cursor);

        if (responses.getRecommendResponseList().isEmpty()) {
            throw new RecommendationException(RecommendationErrorStatus.RECOMMENDATION_NOT_FOUND);
        }

        return responses;
    }

    @Override
    public Recommendation findById(Long recommendationId) {
        return recommendationRepository.findById(recommendationId).orElseThrow(() ->
                new RecommendationException(RecommendationErrorStatus.RECOMMENDATION_NOT_FOUND));
    }
}
