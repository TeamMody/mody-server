package com.example.mody.domain.file.repository;

import com.example.mody.domain.file.entity.BackupFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BackupFileRepository extends JpaRepository<BackupFile, Long> {
}