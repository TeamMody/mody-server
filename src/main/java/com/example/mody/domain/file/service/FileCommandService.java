package com.example.mody.domain.file.service;

import com.example.mody.domain.file.dto.request.BackUpFileRequests;

import java.util.List;

public interface FileCommandService {
    void saveBackupFiles(BackUpFileRequests backupFileRequests);
    void deleteByS3Urls(List<String> s3Urls);
}
