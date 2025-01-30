package com.example.mody.domain.fashionItem.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFashionItem is a Querydsl query type for FashionItem
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFashionItem extends EntityPathBase<FashionItem> {

    private static final long serialVersionUID = -999643865L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFashionItem fashionItem = new QFashionItem("fashionItem");

    public final com.example.mody.global.common.base.QBaseEntity _super = new com.example.mody.global.common.base.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imageUrl = createString("imageUrl");

    public final BooleanPath isLiked = createBoolean("isLiked");

    public final StringPath item = createString("item");

    public final com.example.mody.domain.member.entity.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QFashionItem(String variable) {
        this(FashionItem.class, forVariable(variable), INITS);
    }

    public QFashionItem(Path<? extends FashionItem> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFashionItem(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFashionItem(PathMetadata metadata, PathInits inits) {
        this(FashionItem.class, metadata, inits);
    }

    public QFashionItem(Class<? extends FashionItem> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.example.mody.domain.member.entity.QMember(forProperty("member")) : null;
    }

}

