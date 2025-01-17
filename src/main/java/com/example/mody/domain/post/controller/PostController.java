package com.example.mody.domain.post.controller;

import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.member.repository.MemberRepository;
import io.swagger.v3.oas.annotations.Parameters;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mody.domain.auth.security.CustomUserDetails;
import com.example.mody.domain.post.dto.request.PostCreateRequest;
import com.example.mody.domain.post.exception.annotation.ExistsPost;
import com.example.mody.domain.post.dto.response.PostListResponse;
import com.example.mody.domain.post.service.PostCommandService;
import com.example.mody.domain.post.service.PostQueryService;
import com.example.mody.global.common.base.BaseResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "POST API", description = "게시글 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
@Validated
public class PostController {

    private final PostCommandService postCommandService;
    private final PostQueryService postQueryService;
    private final MemberRepository memberRepository;
	
    @GetMapping
    @Operation(summary = "게시글 목록 조회 API", description = "전체 게시글에 대한 목록 조회 API")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "게시글 목록 조회 성공"
            )
    })
    public BaseResponse<PostListResponse> getAllPosts(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(name = "cursor", required = false) Long cursor,
            @RequestParam(name = "size", defaultValue = "15") Integer size) {
        PostListResponse postListResponse =  postQueryService.getPosts(customUserDetails.getMember(), size, cursor);
        return BaseResponse.onSuccess(postListResponse);
    }

	/**
	 *
	 * @param request
	 * @param customUserDetails
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
            @ApiResponse(
                    responseCode = "POST404",
                    description = "해당 게시물을 찾을 수 없습니다.",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
						{
						    "timestamp": "2024-01-13T10:00:00",
						    "isSuccess": "false",
						    "code": "POST404",
						    "message": "해당 게시물을 찾을 수 없습니다."
						}
						"""
                            )
                    )
            )
    })
    @Parameters({
            @Parameter(name = "posts_id", description = "게시글 아이디, path variable 입니다")
    })
    public BaseResponse<Void> deletePost(
            @PathVariable Long posts_id) {
        postCommandService.deletePost(posts_id);
        return BaseResponse.onSuccessDelete(null);
    }

	@Operation(
		summary = "게시글 좋아요 API",
		description = "특정 게시글에 좋아요를 등록합니다. 이미 좋아요가 눌러져 있다면 좋아요가 취소됩니다.",
		tags = {"게시글", "좋아요"}
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "좋아요 처리 성공",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = BaseResponse.class),
				examples = @ExampleObject(
					value = """
						{
						    "timestamp": "2024-01-14T10:00:00",
						    "code": "COMMON200",
						    "message": "요청에 성공하였습니다.",
						    "result": null
						}
						"""
				)
			)
		),
		@ApiResponse(
			responseCode = "400",
			description = "잘못된 요청",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					value = """
						{
						    "timestamp": "2024-01-14T10:00:00",
						    "code": "COMMON400",
						    "message": "필수 정보가 누락되었습니다.",
						    "result": null
						}
						"""
				)
			)
		),
		@ApiResponse(
			responseCode = "404",
			description = "게시글 또는 사용자를 찾을 수 없음",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					value = """
						{
						    "timestamp": "2024-01-14T10:00:00",
						    "code": "POST404",
						    "message": "해당 게시글을 찾을 수 없습니다.",
						    "result": null
						}
						"""
				)
			)
		)
	})
	public BaseResponse<Void> togglePostLike(
		@PathVariable("post-id")
		@Parameter(
			description = "좋아요를 누를 게시글 ID",
			required = true,
			example = "1"
		) @ExistsPost Long postId,
		@AuthenticationPrincipal
		@Parameter(
			description = "인증된 사용자 정보",
			hidden = true
		) CustomUserDetails customUserDetails
	) {
		Long myId = customUserDetails.getMember().getId();
		postCommandService.togglePostLike(myId, postId);
		return BaseResponse.onSuccess(null);
	}

    @GetMapping("/liked")
    @Operation(summary = "좋아요 누른 목록 조회 API", description = "좋아요 누른 게시글에 대한 목록 조회 API")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "게시글 목록 조회 성공"
            )
    })
    public BaseResponse<PostListResponse> getLikedPosts(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(name = "cursor", required = false) Long cursor,
            @RequestParam(name = "size", defaultValue = "15") Integer size) {
        PostListResponse postListResponse =  postQueryService.getLikedPosts(customUserDetails.getMember(), size, cursor);
        return BaseResponse.onSuccess(postListResponse);
    }

    @GetMapping("/me")
    @Operation(summary = "내가 작성한 게시글 목록 조회 API", description = "내가 작성한 게시글에 대한 목록 조회 API")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "게시글 목록 조회 성공"
            )
    })
    public BaseResponse<PostListResponse> getMyPosts(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(name = "cursor", required = false) Long cursor,
            @RequestParam(name = "size", defaultValue = "15") Integer size) {
        PostListResponse postListResponse =  postQueryService.getMyPosts(customUserDetails.getMember(), size, cursor);
        return BaseResponse.onSuccess(postListResponse);
    }

	@PostMapping("/{posts_id}/reports")
	@Operation(summary = "게시글 신고 API", description = "인증된 유저의 게시글 신고 API")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "게시글 신고 성공"),
			@ApiResponse(
					responseCode = "POST400",
					description = "이미 신고한 게시물 입니다.",
					content = @Content(
							mediaType = "application/json",
							examples = @ExampleObject(
									value = """
						{
						    "timestamp": "2024-01-13T10:00:00",
						    "isSuccess": "false",
						    "code": "POST400",
						    "message": "이미 신고한 게시물 입니다."
						}
						"""
							)
					)
			)
	})
	@Parameters({
			@Parameter(name = "posts_id", description = "게시글 아이디, path variable 입니다")
	})
	public BaseResponse<Void> reportPost(
			@AuthenticationPrincipal CustomUserDetails customUserDetails,
			@PathVariable Long posts_id) {
		postCommandService.reportPost(customUserDetails.getMember(), posts_id);

		return BaseResponse.onSuccess(null);
	}
}
