package com.example.mody.domain.backupimage.service;

import com.example.mody.domain.backupimage.dto.request.BackupFileRequest;
import com.example.mody.domain.backupimage.entity.BackupFile;
import com.example.mody.domain.backupimage.repository.BackupFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BackupFileService {
    private final BackupFileRepository backupFileRepository;

    public void saveBackupFile(BackupFileRequest backupFileRequest){
        BackupFile backupFile = new BackupFile(backupFileRequest.getFileName(),
                backupFileRequest.getFileSize(),
                backupFileRequest.getS3Url());

        backupFileRepository.save(backupFile);
    }
}
