package com.example.mody.domain.member.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = -372438059L;

    public static final QMember member = new QMember("member1");

    public final com.example.mody.global.common.base.QBaseEntity _super = new com.example.mody.global.common.base.QBaseEntity(this);

    public final DatePath<java.time.LocalDate> birthDate = createDate("birthDate", java.time.LocalDate.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final StringPath email = createString("email");

    public final EnumPath<com.example.mody.domain.member.enums.Gender> gender = createEnum("gender", com.example.mody.domain.member.enums.Gender.class);

    public final NumberPath<Integer> height = createNumber("height", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isRegistrationCompleted = createBoolean("isRegistrationCompleted");

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final StringPath profileImageUrl = createString("profileImageUrl");

    public final StringPath provider = createString("provider");

    public final StringPath providerId = createString("providerId");

    public final EnumPath<com.example.mody.domain.member.enums.Role> role = createEnum("role", com.example.mody.domain.member.enums.Role.class);

    public final EnumPath<com.example.mody.domain.member.enums.Status> status = createEnum("status", com.example.mody.domain.member.enums.Status.class);

    public final ListPath<com.example.mody.domain.style.entity.Style, com.example.mody.domain.style.entity.QStyle> styles = this.<com.example.mody.domain.style.entity.Style, com.example.mody.domain.style.entity.QStyle>createList("styles", com.example.mody.domain.style.entity.Style.class, com.example.mody.domain.style.entity.QStyle.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

