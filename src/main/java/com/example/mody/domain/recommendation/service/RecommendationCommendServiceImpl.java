package com.example.mody.domain.recommendation.service;

import com.example.mody.domain.bodytype.entity.mapping.MemberBodyType;
import com.example.mody.domain.bodytype.repository.MemberBodyTypeRepository;
import com.example.mody.domain.chatgpt.service.ChatGptService;
import com.example.mody.domain.exception.BodyTypeException;
import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.recommendation.dto.request.MemberInfoRequest;
import com.example.mody.domain.recommendation.dto.request.RecommendRequest;
import com.example.mody.domain.recommendation.dto.response.RecommendResponse;
import com.example.mody.domain.recommendation.dto.response.analysis.ItemAnalysisResponse;
import com.example.mody.domain.recommendation.dto.response.RecommendLikeResponse;
import com.example.mody.domain.recommendation.dto.response.analysis.StyleAnalysisResponse;
import com.example.mody.domain.recommendation.entity.Recommendation;
import com.example.mody.domain.recommendation.entity.mapping.MemberRecommendationLike;
import com.example.mody.domain.recommendation.enums.RecommendType;
import com.example.mody.domain.recommendation.repository.MemberRecommendationLikeRepository;
import com.example.mody.domain.recommendation.repository.RecommendationRepository;
import com.example.mody.global.common.exception.code.status.BodyTypeErrorStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class RecommendationCommendServiceImpl implements RecommendationCommendService {

    private final MemberBodyTypeRepository memberBodyTypeRepository;
    private final ChatGptService chatGptService;
    private final ObjectMapper objectMapper;
    private final RecommendationRepository recommendationRepository;
    private final RecommendationQueryService recommendationQueryService;
    private final MemberRecommendationLikeRepository memberRecommendationLikeRepository;

    @Override
    public RecommendResponse recommendStyle(Member member, RecommendRequest request) {

        //현재 유저의 bodyType 정보를 받아오기
        MemberBodyType latestBodyType = memberBodyTypeRepository.findTopByMemberOrderByCreatedAt(member)
                .orElseThrow(() -> new BodyTypeException(BodyTypeErrorStatus.MEMBER_BODY_TYPE_NOT_FOUND));

        //gpt 추천 요청을 위한 사용자 정보 구성
        MemberInfoRequest memberInfoRequest = MemberInfoRequest.of(
                member.getNickname(),
                member.getGender(),
                latestBodyType.getBody(),
                latestBodyType.getBodyType().getName());

        //gpt 추천 정보를 받아와 json content를 구성
        StyleAnalysisResponse analysisResponse = chatGptService.recommendGptStyle(memberInfoRequest, request);
        String contentJson = convertToJson(analysisResponse);
        Recommendation newRecommendation = Recommendation.of(
                RecommendType.STYLE,
                analysisResponse.getRecommendedStyle(),
                contentJson,
                analysisResponse.getImageUrl(),
                member
        );

        //추천 데이터 저장
        Recommendation recommendation = recommendationRepository.save(newRecommendation);

        return RecommendResponse.of(
                member.getId(),
                member.getNickname(),
                recommendation.getId(),
                RecommendType.STYLE,
                analysisResponse.getRecommendedStyle(),
                contentJson,
                analysisResponse.getImageUrl()
        );
    }

    @Override
    public RecommendResponse recommendFashionItem(Member member, RecommendRequest request) {

        //현재 유저의 bodyType 정보를 받아오기
        MemberBodyType latestBodyType = memberBodyTypeRepository.findTopByMemberOrderByCreatedAt(member)
                .orElseThrow(() -> new BodyTypeException(BodyTypeErrorStatus.MEMBER_BODY_TYPE_NOT_FOUND));

        //gpt 추천 요청을 위한 사용자 정보 구성
        MemberInfoRequest memberInfoRequest = MemberInfoRequest.of(
                member.getNickname(),
                member.getGender(),
                latestBodyType.getBody(),
                latestBodyType.getBodyType().getName());

        //gpt 추천 결과 받아옴
        ItemAnalysisResponse analysisResponse = chatGptService.recommendGptItem(memberInfoRequest, request);

        Recommendation newRecommendation = Recommendation.of(
                RecommendType.FASHION_ITEM,
                analysisResponse.getItem(),
                analysisResponse.getDescription(),
                analysisResponse.getImageUrl(),
                member
        );

        //추천 결과 저장
        Recommendation recommendation = recommendationRepository.save(newRecommendation);

        return RecommendResponse.of(
                member.getId(),
                member.getNickname(),
                recommendation.getId(),
                RecommendType.FASHION_ITEM,
                analysisResponse.getItem(),
                analysisResponse.getDescription(),
                analysisResponse.getImageUrl()
        );
    }

    @Override
    public RecommendLikeResponse toggleLike(Long recommendationId, Member member) {
        Recommendation recommendation = recommendationQueryService.findById(recommendationId);
        Optional<MemberRecommendationLike> existingLike = memberRecommendationLikeRepository.findTopByMemberAndRecommendation(member, recommendation);

        RecommendLikeResponse response = RecommendLikeResponse.builder()
                .recommendId(recommendationId)
                .memberId(member.getId())
                .build();

        if(existingLike.isPresent()){
            deleteLike(existingLike.get(), recommendation);
            response.setLiked(false);
        } else {
            addLike(member, recommendation);
            response.setLiked(true);
        }
        return response;
    }

    private void addLike(Member member, Recommendation recommendation){
        MemberRecommendationLike newMemberRecommendationLike = new MemberRecommendationLike(member, recommendation);
        memberRecommendationLikeRepository.save(newMemberRecommendationLike);
        increaseLikeCount(recommendation);
    }

    private void increaseLikeCount(Recommendation recommendation){
        recommendation.increaseLikeCount();
        recommendation.getMember()
                .increaseLikeCount();
    }

    private void deleteLike(MemberRecommendationLike memberRecommendationLike, Recommendation recommendation){
        memberRecommendationLikeRepository.delete(memberRecommendationLike);
        decreaseLikeCount(recommendation);
    }

    private void decreaseLikeCount(Recommendation recommendation){
        recommendation.decreaseLikeCount();
        recommendation.getMember()
                .decreaseLikeCount();
    }

    private String convertToJson(StyleAnalysisResponse analysisResponse) {
        try {
            // JSON으로 변환할 데이터 구조
            ContentJson contentJson = new ContentJson(
                    analysisResponse.getIntroduction(),
                    analysisResponse.getStyleDirection(),
                    analysisResponse.getPracticalStylingTips()
            );
            return objectMapper.writeValueAsString(contentJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 변환 오류", e);
        }
    }

    private static class ContentJson {
        public String introduction;
        public String styleDirection;
        public String practicalStylingTips;

        public ContentJson(String introduction, String styleDirection, String practicalStylingTips) {
            this.introduction = introduction;
            this.styleDirection = styleDirection;
            this.practicalStylingTips = practicalStylingTips;
        }
    }
}
