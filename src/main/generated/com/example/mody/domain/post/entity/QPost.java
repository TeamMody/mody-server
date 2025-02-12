package com.example.mody.domain.post.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPost is a Querydsl query type for Post
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPost extends EntityPathBase<Post> {

    private static final long serialVersionUID = 874386081L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPost post = new QPost("post");

    public final com.example.mody.global.common.base.QBaseEntity _super = new com.example.mody.global.common.base.QBaseEntity(this);

    public final com.example.mody.domain.bodytype.entity.QBodyType bodyType;

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<PostImage, QPostImage> images = this.<PostImage, QPostImage>createList("images", PostImage.class, QPostImage.class, PathInits.DIRECT2);

    public final BooleanPath isPublic = createBoolean("isPublic");

    public final NumberPath<Integer> likeCount = createNumber("likeCount", Integer.class);

    public final ListPath<com.example.mody.domain.post.entity.mapping.MemberPostLike, com.example.mody.domain.post.entity.mapping.QMemberPostLike> likes = this.<com.example.mody.domain.post.entity.mapping.MemberPostLike, com.example.mody.domain.post.entity.mapping.QMemberPostLike>createList("likes", com.example.mody.domain.post.entity.mapping.MemberPostLike.class, com.example.mody.domain.post.entity.mapping.QMemberPostLike.class, PathInits.DIRECT2);

    public final com.example.mody.domain.member.entity.QMember member;

    public final NumberPath<Integer> reportCount = createNumber("reportCount", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QPost(String variable) {
        this(Post.class, forVariable(variable), INITS);
    }

    public QPost(Path<? extends Post> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPost(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPost(PathMetadata metadata, PathInits inits) {
        this(Post.class, metadata, inits);
    }

    public QPost(Class<? extends Post> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.bodyType = inits.isInitialized("bodyType") ? new com.example.mody.domain.bodytype.entity.QBodyType(forProperty("bodyType")) : null;
        this.member = inits.isInitialized("member") ? new com.example.mody.domain.member.entity.QMember(forProperty("member")) : null;
    }

}

