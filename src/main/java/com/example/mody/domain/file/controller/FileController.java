package com.example.mody.domain.file.controller;

import com.example.mody.domain.file.dto.request.BackUpFileRequests;
import com.example.mody.domain.file.dto.request.FileCreateResponse;
import com.example.mody.domain.file.service.FileService;
import com.example.mody.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "File API", description = "파일 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
public class FileController {
    private final FileService fileService;

    @PostMapping
    @Operation(summary = "백업 파일 등록 API", description = "파일을 S3에 업로드 한 뒤 해당 s3uri와 파일 정보를 업로드하는 API 입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "백업 파일 등록 성공"),
            @ApiResponse(responseCode = "COMMON402", description = "Validation 관련 예외 - 파일 개수 제한 초과")
    })
    public BaseResponse<Void> createFile(@Valid @RequestBody BackUpFileRequests backUpFileRequests) {
        fileService.saveBackupFiles(backUpFileRequests);
        return BaseResponse.onSuccessCreate(null);
    }

}
