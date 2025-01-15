package com.example.mody.domain.backupimage.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBackupFile is a Querydsl query type for BackupFile
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBackupFile extends EntityPathBase<BackupFile> {

    private static final long serialVersionUID = -1181831408L;

    public static final QBackupFile backupFile = new QBackupFile("backupFile");

    public final com.example.mody.global.common.base.QBaseEntity _super = new com.example.mody.global.common.base.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final StringPath fileName = createString("fileName");

    public final NumberPath<Long> fileSize = createNumber("fileSize", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath s3Url = createString("s3Url");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QBackupFile(String variable) {
        super(BackupFile.class, forVariable(variable));
    }

    public QBackupFile(Path<? extends BackupFile> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBackupFile(PathMetadata metadata) {
        super(BackupFile.class, metadata);
    }

}

