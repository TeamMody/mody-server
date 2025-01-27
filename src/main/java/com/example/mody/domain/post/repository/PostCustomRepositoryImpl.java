package com.example.mody.domain.post.repository;

import com.example.mody.domain.bodytype.entity.BodyType;
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
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.*;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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


    /**
     * 게시글 전체 조회. 조회할 post 목록을 조회하는 쿼리, 해당 목록의 데이터들을 가져오는 쿼리, 각 게시글 별 좋아요 여부 확인 쿼리가 날라감.
     * @param cursorPost
     * @param size
     * @param member
     * @param bodyType
     * @return
     */
    @Override
    public PostListResponse getPostList(Optional<Post> cursorPost, Integer size, Member member, Optional<BodyType> bodyType) {
        BooleanBuilder predicate = new BooleanBuilder();

        NumberExpression<Integer> caseExpression = orderedBodyType(bodyType);

        predicate.and(qPost.isPublic.eq(true)); // 공개여부 == true

        if(cursorPost.isPresent()){
            String customCursor = createCustomCursor(cursorPost.get(), bodyType);
            log.info(customCursor);
            predicate.and(applyCustomCursor(customCursor, bodyType));
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
                .leftJoin(qMemberPostLike).on(qMemberPostLike.post.eq(qPost))
                .where(qPost.id.in(postIds))
                .orderBy(caseExpression.asc(), qPost.createdAt.desc())
                .transform(GroupBy.groupBy(qPost.id).as(
                        Projections.constructor(PostResponse.class,
                                qPost.id,
                                qMember.id,
                                qMember.nickname,
                                qPost.content,
                                qPost.isPublic,
                                qPost.likeCount,
                                isLiked,
                                qPost.bodyType.name,
                                GroupBy.list(qPostImage)
                        )));

        List<PostResponse> postResponses = new ArrayList<>(postResponseMap.values());

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
                                qMember.id,
                                qMember.nickname,
                                qPost.content,
                                qPost.isPublic,
                                qPost.likeCount,
                                Expressions.asBoolean(Expressions.TRUE),
                                qPost.bodyType.name,
                                GroupBy.list(qPostImage)
                        )));

        List<PostResponse> postResponses = new ArrayList<>(postResponseMap.values());

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

        log.info(predicate.toString());

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
                                qMember.id,
                                qMember.nickname,
                                qPost.content,
                                qPost.isPublic,
                                qPost.likeCount,
                                isLikedResult(member),
                                qPost.bodyType.name,
                                GroupBy.list(qPostImage)
                        )));

        List<PostResponse> postResponses = new ArrayList<>(postResponseMap.values());

        return PostListResponse.of(hasNext.apply(postResponses, size),
                postResponses.subList(0, Math.min(size, postResponses.size())));
    }

    private BiFunction<List<PostResponse> , Integer, Boolean> hasNext = (list, size) -> list.size() > size;

    /**
     * // 정렬 기준. 특정 바디 타입이 요구되지 않으면 전부 1
     * @param bodyType
     * @return
     */
    private NumberExpression<Integer> orderedBodyType(Optional<BodyType> bodyType){
        if(bodyType.isPresent()){
            return new CaseBuilder()
                    .when(qPost.bodyType.eq(bodyType.get())).then(0)
                    .otherwise(1);
        }
        return  Expressions.asNumber(1);
    }

    private StringExpression isMatchedBodyType(Optional<BodyType> bodyType){
        if(bodyType.isPresent()){
            return new CaseBuilder()
                    .when(qPost.bodyType.eq(bodyType.get())).then("1")
                    .otherwise("0");
        }
        return  Expressions.asString("0");
    }

    private BooleanExpression isLikedResult(Member member){
        return JPAExpressions
                .selectFrom(qMemberPostLike)
                .where(qMemberPostLike.member.eq(member).and(qMemberPostLike.post.eq(qPost)))
                .exists();
    }

    private String createCustomCursor(Post cursor, Optional<BodyType> bodyType){
        if (cursor == null || bodyType == null) {
            return null;
        }

        String isMatchedBodyType = "0";
        if(bodyType.isPresent()){
            if(cursor.getBodyType().getId().equals(bodyType.get().getId())){
                isMatchedBodyType = "1";
            }
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return isMatchedBodyType + String.format("%19s", formatter.format(cursor.getCreatedAt())).replaceAll(" ","0");
    }

    /**
     * createdAt을 'YYYYMMDDHHmmss' 로 변환
     * @return
     */
    private StringTemplate mySqlDateFormat(){
        return Expressions.stringTemplate(
                "CAST(DATE_FORMAT({0}, {1}) AS STRING)",
                qPost.createdAt,
                ConstantImpl.create("%Y%m%d%H%i%s")
        );
    }

    private StringExpression formatCreatedAt(StringTemplate stringTemplate){
        return StringExpressions.lpad(stringTemplate, 19, '0');
    }

    private BooleanExpression applyCustomCursor(String customCursor, Optional<BodyType> bodyType) {
        if (customCursor == null) { // 커서가 없으면 조건 없음
            return null;
        }

        // bodyType 순서 계산
        StringExpression isMatchedBodyType = isMatchedBodyType(bodyType); // bodyType 일치 여부
        StringTemplate postCreatedAtTemplate = mySqlDateFormat(); //DATE_FORMAT으로 날짜 형태 변경
        StringExpression formattedCreatedAt = formatCreatedAt(postCreatedAtTemplate); // 날짜 형태 변경을 포맷

        return isMatchedBodyType.concat(formattedCreatedAt)
                .lt(customCursor);
    }

}
