package com.example.mody.domain.bodytype.entity.mapping;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberBodyType is a Querydsl query type for MemberBodyType
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberBodyType extends EntityPathBase<MemberBodyType> {

    private static final long serialVersionUID = 1640559059L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberBodyType memberBodyType = new QMemberBodyType("memberBodyType");

    public final com.example.mody.global.common.base.QBaseEntity _super = new com.example.mody.global.common.base.QBaseEntity(this);

    public final StringPath body = createString("body");

    public final com.example.mody.domain.bodytype.entity.QBodyType bodyType;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.example.mody.domain.member.entity.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QMemberBodyType(String variable) {
        this(MemberBodyType.class, forVariable(variable), INITS);
    }

    public QMemberBodyType(Path<? extends MemberBodyType> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberBodyType(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberBodyType(PathMetadata metadata, PathInits inits) {
        this(MemberBodyType.class, metadata, inits);
    }

    public QMemberBodyType(Class<? extends MemberBodyType> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.bodyType = inits.isInitialized("bodyType") ? new com.example.mody.domain.bodytype.entity.QBodyType(forProperty("bodyType")) : null;
        this.member = inits.isInitialized("member") ? new com.example.mody.domain.member.entity.QMember(forProperty("member")) : null;
    }

}

