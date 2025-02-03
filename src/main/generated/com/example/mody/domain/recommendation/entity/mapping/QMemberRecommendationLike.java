package com.example.mody.domain.recommendation.entity.mapping;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberRecommendationLike is a Querydsl query type for MemberRecommendationLike
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberRecommendationLike extends EntityPathBase<MemberRecommendationLike> {

    private static final long serialVersionUID = -2009073692L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberRecommendationLike memberRecommendationLike = new QMemberRecommendationLike("memberRecommendationLike");

    public final com.example.mody.global.common.base.QBaseEntity _super = new com.example.mody.global.common.base.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.example.mody.domain.member.entity.QMember member;

    public final com.example.mody.domain.recommendation.entity.QRecommendation recommendation;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QMemberRecommendationLike(String variable) {
        this(MemberRecommendationLike.class, forVariable(variable), INITS);
    }

    public QMemberRecommendationLike(Path<? extends MemberRecommendationLike> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberRecommendationLike(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberRecommendationLike(PathMetadata metadata, PathInits inits) {
        this(MemberRecommendationLike.class, metadata, inits);
    }

    public QMemberRecommendationLike(Class<? extends MemberRecommendationLike> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.example.mody.domain.member.entity.QMember(forProperty("member")) : null;
        this.recommendation = inits.isInitialized("recommendation") ? new com.example.mody.domain.recommendation.entity.QRecommendation(forProperty("recommendation"), inits.get("recommendation")) : null;
    }

}

