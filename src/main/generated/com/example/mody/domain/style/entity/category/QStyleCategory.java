package com.example.mody.domain.style.entity.category;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QStyleCategory is a Querydsl query type for StyleCategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStyleCategory extends EntityPathBase<StyleCategory> {

    private static final long serialVersionUID = -487305687L;

    public static final QStyleCategory styleCategory = new QStyleCategory("styleCategory");

    public final com.example.mody.global.common.base.QBaseEntity _super = new com.example.mody.global.common.base.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QStyleCategory(String variable) {
        super(StyleCategory.class, forVariable(variable));
    }

    public QStyleCategory(Path<? extends StyleCategory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStyleCategory(PathMetadata metadata) {
        super(StyleCategory.class, metadata);
    }

}

