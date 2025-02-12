package com.example.mody.domain.bodytype.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBodyType is a Querydsl query type for BodyType
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBodyType extends EntityPathBase<BodyType> {

    private static final long serialVersionUID = -1799118151L;

    public static final QBodyType bodyType = new QBodyType("bodyType");

    public final com.example.mody.global.common.base.QBaseEntity _super = new com.example.mody.global.common.base.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<com.example.mody.domain.bodytype.entity.mapping.MemberBodyType, com.example.mody.domain.bodytype.entity.mapping.QMemberBodyType> memberBodyTypeList = this.<com.example.mody.domain.bodytype.entity.mapping.MemberBodyType, com.example.mody.domain.bodytype.entity.mapping.QMemberBodyType>createList("memberBodyTypeList", com.example.mody.domain.bodytype.entity.mapping.MemberBodyType.class, com.example.mody.domain.bodytype.entity.mapping.QMemberBodyType.class, PathInits.DIRECT2);

    public final StringPath name = createString("name");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QBodyType(String variable) {
        super(BodyType.class, forVariable(variable));
    }

    public QBodyType(Path<? extends BodyType> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBodyType(PathMetadata metadata) {
        super(BodyType.class, metadata);
    }

}

