package com.example.mody.domain.post.repository;

import com.example.mody.domain.bodytype.entity.BodyType;
import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.member.entity.QMember;
import com.example.mody.domain.post.dto.response.PostListResponse;
import com.example.mody.domain.post.dto.response.PostResponse;
import com.example.mody.domain.post.entity.QPost;
import com.example.mody.domain.post.entity.QPostImage;
import com.example.mody.domain.post.entity.mapping.QMemberPostLike;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.function.BiFunction;

@Repository
@RequiredArgsConstructor
@Slf4j
public class PostCustomRepositoryImpl implements PostCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    private final QPost qPost = QPost.post;
    private final QMember qMember = QMember.member;
    private final QMemberPostLike qMemberPostLike = QMemberPostLike.memberPostLike;
    private final QPostImage qPostImage = QPostImage.postImage;


    /**
     * 게시글 전체 조회. 조회할 post 목록을 조회하는 쿼리, 해당 목록의 데이터들을 가져오는 쿼리, 각 게시글 별 좋아요 여부 확인 쿼리가 날라감.
     * @param cursor
     * @param size
     * @param member
     * @param bodyType
     * @return
     */
    @Override
    public PostListResponse getPostList(Long cursor, Integer size, Member member, Optional<BodyType> bodyType) {
        BooleanBuilder predicate = new BooleanBuilder();

        NumberExpression<Integer> caseExpression = bodyTypeOrderCase(bodyType);

        predicate.and(qPost.isPublic.eq(true)); // 공개여부 == true

        if(cursor != null){
            predicate.and(qPost.id.lt(cursor));
        }

        BooleanExpression isLiked = Expressions.asBoolean(false);
        if (member != null){
            isLiked = isLikedResult(member);
        }

        List<Long> postIds = jpaQueryFactory
                .select(qPost.id)
                .from(qPost)
                .where(predicate)
                .orderBy(caseExpression.asc(), qPost.createdAt.desc())
                .limit(size+1) //하나 더 가져와서 다음 요소가 존재하는지 확인
                .fetch();

        Map<Long, PostResponse> postResponseMap = jpaQueryFactory
                .from(qPost)
                .leftJoin(qPost.member, qMember)
                .leftJoin(qPost.images, qPostImage)
                .where(qPost.id.in(postIds))
                .orderBy(caseExpression.asc(), qPost.createdAt.desc())
                .transform(GroupBy.groupBy(qPost.id).as(
                        Projections.constructor(PostResponse.class,
                                qPost.id,
                                qMember.nickname,
                                qPost.content,
                                qPost.isPublic,
                                qPost.likeCount,
                                isLiked,
                                qPost.bodyType.name,
                                GroupBy.list(qPostImage)
                        )));

        List<PostResponse> postResponses = new ArrayList<>(postResponseMap.values());

        return PostListResponse.from(hasNext.apply(postResponses, size),
                postResponses.subList(0, Math.min(size, postResponses.size())));
    }

    /**
     * 좋아요 누른 게시글 목록 조회
     * @param cursor
     * @param size
     * @param member
     * @return
     */
    public PostListResponse getLikedPosts(Long cursor, Integer size, Member member) {
        BooleanBuilder predicate = new BooleanBuilder();

        if(cursor != null){
            predicate.and(qPost.id.lt(cursor));
        }

        predicate.and(qMemberPostLike.member.eq(member));

        List<Long> postIds = jpaQueryFactory
                .select(qPost.id)
                .from(qPost)
                .leftJoin(qMemberPostLike).on(qMemberPostLike.post.eq(qPost).and(qMemberPostLike.member.eq(member)))
                .where(predicate)
                .orderBy(qPost.createdAt.desc())
                .limit(size+1) //하나 더 가져와서 다음 요소가 존재하는지 확인
                .fetch();

        Map<Long, PostResponse> postResponseMap = jpaQueryFactory
                .from(qPost)
                .leftJoin(qPost.member, qMember)
                .leftJoin(qPost.images, qPostImage)
                .where(qPost.id.in(postIds))
                .orderBy(qPost.createdAt.desc())
                .transform(GroupBy.groupBy(qPost.id).as(
                        Projections.constructor(PostResponse.class,
                                qPost.id,
                                qMember.nickname,
                                qPost.content,
                                qPost.isPublic,
                                qPost.likeCount,
                                Expressions.asBoolean(Expressions.TRUE),
                                qPost.bodyType.name,
                                GroupBy.list(qPostImage)
                        )));

        List<PostResponse> postResponses = new ArrayList<>(postResponseMap.values());

        return PostListResponse.from(hasNext.apply(postResponses, size),
                postResponses.subList(0, Math.min(size, postResponses.size())));
    }

    @Override
    public PostListResponse getMyPosts(Long cursor, Integer size, Member member) {
        BooleanBuilder predicate = new BooleanBuilder();

        if(cursor != null){
            predicate.and(qPost.id.lt(cursor));
        }

        List<Long> postIds = jpaQueryFactory
                .select(qPost.id)
                .from(qPost)
                .where(predicate)
                .orderBy(qPost.createdAt.desc())
                .limit(size+1) //하나 더 가져와서 다음 요소가 존재하는지 확인
                .fetch();

        Map<Long, PostResponse> postResponseMap = jpaQueryFactory
                .from(qPost)
                .leftJoin(qPost.member, qMember)
                .leftJoin(qPost.images, qPostImage)
                .leftJoin(qMemberPostLike).on(qMemberPostLike.post.eq(qPost))
                .where(qPost.id.in(postIds))
                .orderBy( qPost.createdAt.desc())
                .transform(GroupBy.groupBy(qPost.id).as(
                        Projections.constructor(PostResponse.class,
                                qPost.id,
                                qMember.nickname,
                                qPost.content,
                                qPost.isPublic,
                                qPost.likeCount,
                                JPAExpressions
                                        .selectFrom(qMemberPostLike)
                                        .where(qMemberPostLike.member.eq(member).and(qMemberPostLike.post.eq(qPost)))
                                        .exists(),
                                qPost.bodyType.name,
                                GroupBy.list(qPostImage)
                        )));

        List<PostResponse> postResponses = new ArrayList<>(postResponseMap.values());

        return PostListResponse.from(hasNext.apply(postResponses, size),
                postResponses.subList(0, Math.min(size, postResponses.size())));
    }

    private BiFunction<List<PostResponse> , Integer, Boolean> hasNext = (list, size) -> list.size() > size;

    /**
     * // 정렬 기준. 특정 바디 타입이 요구되지 않으면 전부 1
     * @param bodyType
     * @return
     */
    private NumberExpression<Integer> bodyTypeOrderCase(Optional<BodyType> bodyType){
        if(bodyType.isPresent()){
            return new CaseBuilder()
                    .when(qPost.bodyType.eq(bodyType.get())).then(0)
                    .otherwise(1);
        }
        return  Expressions.asNumber(1);
    }

    private BooleanExpression isLikedResult(Member member){
        return JPAExpressions.selectFrom(qMemberPostLike)
                .where(qMemberPostLike.post.eq(qPost)
                        .and(qMemberPostLike.member.eq(member)))
                .exists();
    }
}
