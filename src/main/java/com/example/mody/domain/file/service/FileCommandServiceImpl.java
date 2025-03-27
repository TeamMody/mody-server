package com.example.mody.domain.file.service;

import com.example.mody.domain.file.dto.request.BackUpFileRequests;
import com.example.mody.domain.file.entity.BackupFile;
import com.example.mody.domain.file.repository.BackupFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FileCommandServiceImpl implements FileCommandService{

    private final BackupFileRepository backupFileRepository;

    @Transactional
    public void saveBackupFiles(BackUpFileRequests backupFileRequests){
        backupFileRepository.saveAll(backupFileRequests.getFiles().stream()
                .map(file -> new BackupFile(file.getFileName(),
                                file.getFileSize(),
                                file.getS3Url()))
                .toList());
    }

    public void deleteByS3Urls(List<String> s3Urls){
        backupFileRepository.deleteAllByS3UrlIn(s3Urls);
    }

}
