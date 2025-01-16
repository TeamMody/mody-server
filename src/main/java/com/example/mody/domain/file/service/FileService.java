package com.example.mody.domain.file.service;

import com.example.mody.domain.file.dto.request.BackupFileRequest;
import com.example.mody.domain.file.dto.request.FileCreateResponse;
import com.example.mody.domain.file.entity.BackupFile;
import com.example.mody.domain.file.repository.BackupFileRepository;
import com.example.mody.global.util.S3FileComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileService {

    private final BackupFileRepository backupFileRepository;

    public void saveBackupFile(BackupFileRequest backupFileRequest){
        BackupFile backupFile = new BackupFile(backupFileRequest.getFileName(),
                backupFileRequest.getFileSize(),
                backupFileRequest.getS3Url());

        backupFileRepository.save(backupFile);
    }


}
