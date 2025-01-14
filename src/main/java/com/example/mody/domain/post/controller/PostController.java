package com.example.mody.domain.post.controller;

import com.example.mody.domain.auth.security.CustomUserDetails;
import com.example.mody.domain.post.dto.request.PostCreateRequest;
import com.example.mody.domain.post.service.PostCommandService;
import com.example.mody.global.common.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "POST API", description = "게시글 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostCommandService postCommandService;

    /**
     *
     * @param request
     * @param customUserDetails 현재는 동작하지 않으므로 SecurityContextHolder에서 직접 memberId를 추출하여 사용함. memberId로 member를 찾는 부분도 서비스 단으로 들어가는게 좋다고 판단되지만, 인증 부분이
     * @return
     */
    @PostMapping
    @Operation(summary = "게시글 작성 API", description = "인증된 유저의 게시글 작성 API")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "게시글 작성 성공"
            )
    })
    public BaseResponse<Void> registerPost(
            @Valid @RequestBody PostCreateRequest request, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        postCommandService.createPost(request, customUserDetails.getMember());
        return BaseResponse.onSuccessCreate(null);
    }


    @DeleteMapping("/{posts_id}")
    @Operation(summary = "게시글 삭제 API", description = "인증된 유저의 게시글 삭제 API")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "게시글 삭제 성공"),
    })
    @Parameters({
            @Parameter(name = "posts_id", description = "게시글 아이디, path variable 입니다")
    })
    public BaseResponse<Void> deletePost(
            @PathVariable Long posts_id) {
        postCommandService.deletePost(posts_id);
        return BaseResponse.onSuccessDelete(null);
    }

}
