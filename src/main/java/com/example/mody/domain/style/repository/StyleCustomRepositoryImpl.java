package com.example.mody.domain.style.repository;

import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.member.entity.QMember;
import com.example.mody.domain.style.dto.response.StyleRecommendResponse;
import com.example.mody.domain.style.dto.response.StyleRecommendResponses;
import com.example.mody.domain.style.entity.QStyle;
import com.example.mody.domain.style.entity.mapping.QMemberStyleLike;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.function.BiFunction;

@Repository
@RequiredArgsConstructor
public class StyleCustomRepositoryImpl implements StyleCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QMember qMember = QMember.member;
    private final QMemberStyleLike qMemberStyleLike = QMemberStyleLike.memberStyleLike;
    private final QStyle qStyle = QStyle.style;

    @Override
    public StyleRecommendResponses findMyStyleRecommends(Member member, Integer size, Long cursor) {
        BooleanBuilder predicate = new BooleanBuilder();

        if(cursor != null){
            predicate.and(qStyle.id.lt(cursor));
        }

        List<StyleRecommendResponse> styleRecommendResponseList = jpaQueryFactory
                .select(Projections.constructor(
                        StyleRecommendResponse.class,
                        qStyle.id,
                        qMember.id,
                        qMember.nickname,
                        qMemberStyleLike.isNotNull(),
                        qStyle.likeCount,
                        qStyle.recommendedStyle,
                        qStyle.introduction,
                        qStyle.styleDirection,
                        qStyle.practicalStylingTips,
                        qStyle.imageUrl
                        ))
                .from(qStyle)
                .leftJoin(qStyle.member, qMember)
                .leftJoin(qMemberStyleLike).on(qMemberStyleLike.member.eq(member).and(qMemberStyleLike.style.eq(qStyle)))
                .where(qStyle.member.eq(member)
                        .and(predicate))
                .orderBy(qStyle.id.desc())
                .limit(size+1)
                .fetch();

        return StyleRecommendResponses.of(hasNext.apply(styleRecommendResponseList, size),
                styleRecommendResponseList.subList(0, Math.min(size, styleRecommendResponseList.size())));
    }

    private BiFunction<List<StyleRecommendResponse> , Integer, Boolean> hasNext = (list, size) -> list.size() > size;
}
