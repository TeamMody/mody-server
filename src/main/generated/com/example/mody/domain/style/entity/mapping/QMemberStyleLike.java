package com.example.mody.domain.style.entity.mapping;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberStyleLike is a Querydsl query type for MemberStyleLike
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberStyleLike extends EntityPathBase<MemberStyleLike> {

    private static final long serialVersionUID = 1563108868L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberStyleLike memberStyleLike = new QMemberStyleLike("memberStyleLike");

    public final com.example.mody.global.common.base.QBaseEntity _super = new com.example.mody.global.common.base.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.example.mody.domain.member.entity.QMember member;

    public final com.example.mody.domain.style.entity.QStyle style;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QMemberStyleLike(String variable) {
        this(MemberStyleLike.class, forVariable(variable), INITS);
    }

    public QMemberStyleLike(Path<? extends MemberStyleLike> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberStyleLike(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberStyleLike(PathMetadata metadata, PathInits inits) {
        this(MemberStyleLike.class, metadata, inits);
    }

    public QMemberStyleLike(Class<? extends MemberStyleLike> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.example.mody.domain.member.entity.QMember(forProperty("member")) : null;
        this.style = inits.isInitialized("style") ? new com.example.mody.domain.style.entity.QStyle(forProperty("style"), inits.get("style")) : null;
    }

}

