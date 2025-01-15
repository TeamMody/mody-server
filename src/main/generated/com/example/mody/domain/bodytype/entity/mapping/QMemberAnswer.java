package com.example.mody.domain.bodytype.entity.mapping;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberAnswer is a Querydsl query type for MemberAnswer
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberAnswer extends EntityPathBase<MemberAnswer> {

    private static final long serialVersionUID = 571481941L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberAnswer memberAnswer = new QMemberAnswer("memberAnswer");

    public final com.example.mody.global.common.base.QBaseEntity _super = new com.example.mody.global.common.base.QBaseEntity(this);

    public final com.example.mody.domain.bodytype.entity.QAnswer answer;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.example.mody.domain.member.entity.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QMemberAnswer(String variable) {
        this(MemberAnswer.class, forVariable(variable), INITS);
    }

    public QMemberAnswer(Path<? extends MemberAnswer> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberAnswer(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberAnswer(PathMetadata metadata, PathInits inits) {
        this(MemberAnswer.class, metadata, inits);
    }

    public QMemberAnswer(Class<? extends MemberAnswer> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.answer = inits.isInitialized("answer") ? new com.example.mody.domain.bodytype.entity.QAnswer(forProperty("answer"), inits.get("answer")) : null;
        this.member = inits.isInitialized("member") ? new com.example.mody.domain.member.entity.QMember(forProperty("member")) : null;
    }

}

