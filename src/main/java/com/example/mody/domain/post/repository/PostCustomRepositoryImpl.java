package com.example.mody.domain.post.repository;

import com.example.mody.domain.bodytype.entity.BodyType;
import com.example.mody.domain.bodytype.entity.QBodyType;
import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.member.entity.QMember;
import com.example.mody.domain.post.dto.response.PostListResponse;
import com.example.mody.domain.post.dto.response.PostResponse;
import com.example.mody.domain.post.dto.response.recode.LikedPostsResponse;
import com.example.mody.domain.post.entity.Post;
import com.example.mody.domain.post.entity.QPost;
import com.example.mody.domain.post.entity.QPostImage;
import com.example.mody.domain.post.entity.mapping.QMemberPostLike;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.*;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.QueryHint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.QueryHints;
import org.springframework.stereotype.Repository;

import java.time.format.DateTimeFormatter;
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
    private final QBodyType qBodyType = QBodyType.bodyType;


    /**
     * 게시글 전체 조회. 조회할 post 목록을 조회하는 쿼리, 해당 목록의 데이터들을 가져오는 쿼리, 각 게시글 별 좋아요 여부 확인 쿼리가 날라감.
     * @param cursorPost
     * @param size
     * @param member
     * @param bodyType
     * @return
     */
    @Override
    public PostListResponse getBodyTypePosts(Optional<Post> cursorPost, Integer size, Member member, BodyType bodyType) {
        BooleanBuilder predicate = new BooleanBuilder();

        predicate.and(qPost.isPublic.eq(true)); // 공개여부 == true
        predicate.and(qPost.bodyType.eq(bodyType));

        if(cursorPost.isPresent()){
            predicate.and(qPost.id.lt(cursorPost.get().getId()));
        }

        //동적 정렬
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
        orderSpecifiers.add(qPost.id.desc()); // id를 auto_increment 를 사용하므로 created_at을 대신하여 id로 최신순 정렬을 함

        List<Long> postIds = getPostIds(predicate, size, orderSpecifiers);
        List<PostResponse> postResponses = getPostResponsesByIds(postIds, member, orderSpecifiers);

        // 여기서는 목록 개수가 size 개수와 동일한 경우도 hasNext가 true임.
        return PostListResponse.of(hasNext.apply(postResponses, size-1),
                postResponses.subList(0, Math.min(size, postResponses.size())));
    }

    @Override
    public PostListResponse getOtherBodyTypePosts(Optional<Post> cursorPost, Integer size, Member member, BodyType bodyType) {
        BooleanBuilder predicate = new BooleanBuilder();

        predicate.and(qPost.isPublic.eq(true)); // 공개여부 == true
        predicate.and(qPost.bodyType.ne(bodyType));

        if(cursorPost.isPresent()){
            predicate.and(qPost.id.lt(cursorPost.get().getId()));
        }

        //동적 정렬
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
        orderSpecifiers.add(qPost.id.desc());

        List<Long> postIds = getPostIds(predicate, size+1, orderSpecifiers); //하나 더 가져와서 다음 요소가 존재하는지 확인
        List<PostResponse> postResponses = getPostResponsesByIds(postIds, member, orderSpecifiers);

        return PostListResponse.of(hasNext.apply(postResponses, size),
                postResponses.subList(0, Math.min(size, postResponses.size())));
    }

    /**
     * 좋아요 누른 게시글 목록 조회
     * @param cursor 좋아요의 id
     * @param size
     * @param member
     * @return
     */
    public LikedPostsResponse getLikedPosts(Long cursor, Integer size, Member member) {
        BooleanBuilder predicate = new BooleanBuilder();

        if(cursor != null){
            predicate.and(qMemberPostLike.id.lt(cursor));
        }

        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
        orderSpecifiers.add(qMemberPostLike.id.desc()); // 최근에 누른 좋아요 순으로 정렬

        List<Long> postIds = getMemberLikedPostIds(
                predicate, size+1, member, orderSpecifiers);
        List<PostResponse> postResponses = getLikedPostResponsesByIds(postIds, member);

        return new LikedPostsResponse(
                hasNext.apply(postResponses, size),
                postResponses.subList(0, Math.min(size, postResponses.size())));
    }

    @Override
    public PostListResponse getMyPosts(Long cursor, Integer size, Member member) {
        BooleanBuilder predicate = new BooleanBuilder();

        predicate.and(qPost.member.eq(member));

        if(cursor != null){
            predicate.and(qPost.id.lt(cursor));
        }

        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
        orderSpecifiers.add(qPost.id.desc());

        List<Long> postIds = getPostIds(predicate, size+1, orderSpecifiers);
        List<PostResponse> postResponses = getPostResponsesByIds(postIds, member, orderSpecifiers);

        return PostListResponse.of(hasNext.apply(postResponses, size),
                postResponses.subList(0, Math.min(size, postResponses.size())));
    }

    @Override
    public PostListResponse getRecentPosts(Long cursor, Integer size, Member member) {
        BooleanBuilder predicate = new BooleanBuilder();

        predicate.and(qPost.isPublic.eq(true));

        if(cursor != null){
            predicate.and(qPost.id.lt(cursor));
        }

        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
        orderSpecifiers.add(qPost.id.desc());

        List<Long> postIds = getPostIds(predicate, size+1, orderSpecifiers);
        List<PostResponse> postResponses = getPostResponsesByIds(postIds, member, orderSpecifiers);

        return PostListResponse.of(hasNext.apply(postResponses, size),
                postResponses.subList(0, Math.min(size, postResponses.size())));
    }

    private List<Long> getPostIds(BooleanBuilder predicate, Integer size,
                                  List<OrderSpecifier<?>> orderSpecifiers){
        return jpaQueryFactory
                .select(qPost.id)
                .from(qPost)
                .where(predicate)
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
                .limit(size)
                .fetch();
    }

    private List<Long> getMemberLikedPostIds(BooleanBuilder predicate, Integer size, Member member,
                                       List<OrderSpecifier<?>> orderSpecifiers){
        return jpaQueryFactory
                .select(qPost.id)
                .from(qPost)
                .innerJoin(qMemberPostLike).on(qMemberPostLike.post.eq(qPost).and(qMemberPostLike.member.eq(member)))
                .where(predicate)
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
                .limit(size)
                .fetch();
    }
    private List<PostResponse> getPostResponsesByIds (List<Long> postIds, Member member,
                                                List<OrderSpecifier<?>> orderSpecifiers){
        Map<Long, PostResponse> postResponseMap = jpaQueryFactory
                .from(qPost)
                .innerJoin(qPost.member, qMember)
                .leftJoin(qPost.images, qPostImage)
                .leftJoin(qMemberPostLike).on(qMemberPostLike.post.eq(qPost).and(qMemberPostLike.member.eq(member)))
                .innerJoin(qPost.bodyType, qBodyType)
                .where(qPost.id.in(postIds))
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
                .transform(GroupBy.groupBy(qPost.id).as(
                        Projections.constructor(PostResponse.class,
                                qPost.id,
                                qMember.id,
                                qMember.nickname,
                                qMember.eq(member),
                                qPost.content,
                                qPost.isPublic,
                                qPost.likeCount,
                                qMemberPostLike.isNotNull(),
                                qPost.bodyType.name,
                                GroupBy.list(qPostImage)
                        )));

        List<PostResponse> postResponses = new ArrayList<>(postResponseMap.values());
        return postResponses;
    }

    private List<PostResponse> getLikedPostResponsesByIds (List<Long> postIds, Member member){
        Map<Long, PostResponse> postResponseMap = jpaQueryFactory
                .from(qPost)
                .innerJoin(qPost.member, qMember)
                .leftJoin(qPost.images, qPostImage)
                .innerJoin(qPost.bodyType, qBodyType)
                .where(qPost.id.in(postIds))
                .transform(GroupBy.groupBy(qPost.id).as(
                        Projections.constructor(PostResponse.class,
                                qPost.id,
                                qMember.id,
                                qMember.nickname,
                                qMember.eq(member),
                                qPost.content,
                                qPost.isPublic,
                                qPost.likeCount,
                                Expressions.asBoolean(Expressions.TRUE),
                                qPost.bodyType.name,
                                GroupBy.list(qPostImage)
                        )));

        return orderByPostIds(postIds, postResponseMap);
    }

    private List<PostResponse> orderByPostIds(List<Long> postIds, Map<Long, PostResponse> postResponseMap){
        return postIds.stream()
                .map(postResponseMap::get)
                .filter(Objects::nonNull)
                .toList();
    }

    private BiFunction<List<PostResponse> , Integer, Boolean> hasNext = (list, size) -> list.size() > size;

}
