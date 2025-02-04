package com.example.mody.domain.recommendation.entity.category;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAppealCategory is a Querydsl query type for AppealCategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAppealCategory extends EntityPathBase<AppealCategory> {

    private static final long serialVersionUID = -2052142589L;

    public static final QAppealCategory appealCategory = new QAppealCategory("appealCategory");

    public final com.example.mody.global.common.base.QBaseEntity _super = new com.example.mody.global.common.base.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QAppealCategory(String variable) {
        super(AppealCategory.class, forVariable(variable));
    }

    public QAppealCategory(Path<? extends AppealCategory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAppealCategory(PathMetadata metadata) {
        super(AppealCategory.class, metadata);
    }

}

