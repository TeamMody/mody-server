package com.example.mody.domain.recommendation.repository;

import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.member.entity.QMember;
import com.example.mody.domain.recommendation.dto.response.QRecommendResponse;
import com.example.mody.domain.recommendation.dto.response.RecommendResponse;
import com.example.mody.domain.recommendation.dto.response.RecommendResponses;
import com.example.mody.domain.recommendation.dto.response.analysis.StyleAnalysisResponse;
import com.example.mody.domain.recommendation.entity.QRecommendation;
import com.example.mody.domain.recommendation.entity.mapping.QMemberRecommendationLike;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.function.BiFunction;

@RequiredArgsConstructor
public class RecommendCustomRepositoryImpl implements RecommendCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;
    private final QMember qMember = QMember.member;
    private final QMemberRecommendationLike qMemberRecommendationLike = QMemberRecommendationLike.memberRecommendationLike;
    private final QRecommendation qRecommendation = QRecommendation.recommendation;
    @Override
    public RecommendResponses findMyRecommendations(Member member, Integer size, Long cursor) {
        BooleanBuilder predicate = new BooleanBuilder();

        if(cursor != null){
            predicate.and(qRecommendation.id.lt(cursor));
        }

        List<RecommendResponse> recommendResponseList = jpaQueryFactory
                .select(new QRecommendResponse(
                        qMember.id,
                        qMember.nickname,
                        qRecommendation.id,
                        qRecommendation.recommendType,
                        qMemberRecommendationLike.isNotNull(),
                        qRecommendation.title,
                        qRecommendation.content,
                        qRecommendation.imageUrl
                ))
                .from(qRecommendation)
                .leftJoin(qRecommendation.member, qMember)
                .leftJoin(qMemberRecommendationLike)
                .on(qMemberRecommendationLike.member.eq(member).and(qMemberRecommendationLike.recommendation.eq(qRecommendation)))
                .where(qRecommendation.member.eq(member)
                        .and(predicate))
                .orderBy(qRecommendation.id.desc())
                .limit(size+1)
                .fetch();

        return RecommendResponses.of(hasNext(recommendResponseList, size),
                recommendResponseList.subList(0, Math.min(size, recommendResponseList.size())));
    }

    private boolean hasNext(List<RecommendResponse> list, int size) {
        return list.size() > size;
    }
}
