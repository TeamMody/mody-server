package com.example.mody.domain.post.entity.mapping;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberPostLike is a Querydsl query type for MemberPostLike
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberPostLike extends EntityPathBase<MemberPostLike> {

    private static final long serialVersionUID = -1295047182L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberPostLike memberPostLike = new QMemberPostLike("memberPostLike");

    public final com.example.mody.global.common.base.QBaseEntity _super = new com.example.mody.global.common.base.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.example.mody.domain.member.entity.QMember member;

    public final com.example.mody.domain.post.entity.QPost post;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QMemberPostLike(String variable) {
        this(MemberPostLike.class, forVariable(variable), INITS);
    }

    public QMemberPostLike(Path<? extends MemberPostLike> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberPostLike(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberPostLike(PathMetadata metadata, PathInits inits) {
        this(MemberPostLike.class, metadata, inits);
    }

    public QMemberPostLike(Class<? extends MemberPostLike> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.example.mody.domain.member.entity.QMember(forProperty("member")) : null;
        this.post = inits.isInitialized("post") ? new com.example.mody.domain.post.entity.QPost(forProperty("post"), inits.get("post")) : null;
    }

}

