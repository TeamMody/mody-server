package com.example.mody.domain.backupimage.repository;

import com.example.mody.domain.backupimage.entity.BackupFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BackupFileRepository extends JpaRepository<BackupFile, Long> {
}