package com.example.mody.domain.file.controller;

import com.example.mody.domain.file.dto.request.FileCreateResponse;
import com.example.mody.domain.file.service.FileService;
import com.example.mody.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "File API", description = "파일 관련 API")
//@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
public class FileController {
    private final FileService fileService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "파일 생성 API", description = "파일을 생성하는 API입니다. jpg, mp3, mp4 등 다양한 파일을 업로드할 수 있습니다.")
    public BaseResponse<FileCreateResponse> createFile
            (
                    @Valid @Parameter(
                            content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE) )
                    @RequestPart("file") MultipartFile file
            )
    {
        return BaseResponse.onSuccess(fileService.createFile("", file));
    }

}
