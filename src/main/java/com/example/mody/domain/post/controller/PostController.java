package com.example.mody.domain.post.controller;

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
import com.example.mody.domain.post.service.PostCommandService;
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
import lombok.RequiredArgsConstructor;

@Tag(name = "POST API", description = "게시글 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
@Validated
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
}
