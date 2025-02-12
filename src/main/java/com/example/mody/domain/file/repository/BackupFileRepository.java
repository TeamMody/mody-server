package com.example.mody.domain.file.repository;

import com.example.mody.domain.file.entity.BackupFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BackupFileRepository extends JpaRepository<BackupFile, Long> {
    void deleteAllByIdIn(List<Long> backupImageIds);

    void deleteAllByS3UrlIn(List<String> s3Urls);
}