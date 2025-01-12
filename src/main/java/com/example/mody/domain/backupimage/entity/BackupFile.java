package com.example.mody.domain.backupimage.entity;

import com.example.mody.global.common.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BackupFile extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "backup_image_id")
    private Long id;

    private String fileName;
    private Long fileSize;
    private String s3Url;

    public BackupFile(String fileName, Long fileSize, String s3Url){
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.s3Url = s3Url;
    }

}
