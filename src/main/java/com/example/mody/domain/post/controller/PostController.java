package com.example.mody.domain.post.controller;


import com.example.mody.domain.member.repository.MemberRepository;
import com.example.mody.domain.post.dto.request.PostUpdateRequest;
import com.example.mody.domain.post.dto.response.PostResponse;
import io.swagger.v3.oas.annotations.Parameters;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.mody.domain.auth.security.CustomUserDetails;
import com.example.mody.domain.post.dto.request.PostCreateRequest;
import com.example.mody.domain.post.dto.response.PostResponses;
import com.example.mody.domain.post.exception.annotation.ExistsPost;
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
import lombok.RequiredArgsConstructor;

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
	public BaseResponse<PostResponses> getAllPosts(
		@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@RequestParam(name = "cursor", required = false) Long cursor,
		@RequestParam(name = "size", defaultValue = "15") Integer size) {
		PostResponses postResponses = postQueryService.getPosts(customUserDetails.getMember(), size, cursor);
		return BaseResponse.onSuccess(postResponses);
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
			@ApiResponse(responseCode = "201", description = "게시글 작성 성공"),
			@ApiResponse(responseCode = "MEMBER_BODY_TYPE404", description = "게시글을 작성하려는 유저가 아직 체형 분석을 진행하지 않은 경우"),
			@ApiResponse(responseCode = "COMMON402", description = "Validation 관련 예외 - 파일 개수 제한 초과, content 길이 제한 초과"),
			@ApiResponse(
					responseCode = "400",
					description = "S3 url 목록은 비어있을 수 없습니다. 파일을 선택하거나, presigned url 생성 api를 재요청해주세요.",
					content = @Content(
							mediaType = "application/json",
							examples = @ExampleObject(
									value = """
						{
							"timestamp": "2025-01-26T15:15:54.334Z",
							"code": "COMMON402",
							"message": "Validation Error입니다.",
							"result": {
								"s3Urls": "S3 url 목록은 비어 있을 수 없습니다."
							}
						}
						"""
							)
					)
			),
			@ApiResponse(
					responseCode = "404",
					description = "올바르지 않은 S3 url입니다.",
					content = @Content(
							mediaType = "application/json",
							examples = @ExampleObject(
									value = """
						{
							"timestamp": "2025-01-26T15:15:54.334Z",
							"code": "S3_404",
							"message": "요청한 S3 객체를 찾을 수 없습니다."
						}
						"""
							)
					)
			),
	})
	public BaseResponse<Void> registerPost(
		@Valid @RequestBody PostCreateRequest request, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
		postCommandService.createPost(request, customUserDetails.getMember());
		return BaseResponse.onSuccessCreate(null);
	}

    @DeleteMapping("/{postId}")
    @Operation(summary = "게시글 삭제 API", description = "인증된 유저의 게시글 삭제 API")
    @ApiResponses({
            @ApiResponse(responseCode = "COMMON202", description = "게시글 삭제 성공"),
			@ApiResponse(
					responseCode = "COMMON401",
					description = "로그인이 필요합니다.",
					content = @Content(
							mediaType = "application/json",
							examples = @ExampleObject(
									value = """
					{
					  "timestamp": "2025-01-26T21:23:51.4515304",
					  "code": "COMMON401",
					  "message": "인증이 필요합니다."
					}
					"""
							)
					)
			),
			@ApiResponse(
					responseCode = "POST403",
					description = "작성자가 아닌 유저의 요청으로 권한이 없는 경우",
					content = @Content(
							mediaType = "application/json",
							examples = @ExampleObject(
									value = """
          {
            "timestamp": "2025-01-27T20:56:55.7942672",
            "code": "POST403",
            "message": "해당 게시글에 대한 권한이 없습니다."
          }
					"""
							)
					)
			),       
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
            @Parameter(name = "postId", description = "게시글 아이디, path variable 입니다")
    })
    public BaseResponse<Void> deletePost(
            @PathVariable(name = "postId") Long postId,
			@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        postCommandService.deletePost(postId, customUserDetails.getMember());
        return BaseResponse.onSuccessDelete(null);
    }

	@Operation(
		summary = "게시글 좋아요 API",
		description = "특정 게시글에 좋아요를 등록합니다. 이미 좋아요가 눌러져 있다면 좋아요가 취소됩니다."
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
	@PostMapping("/{postId}/like")
	public BaseResponse<Void> togglePostLike(
		@PathVariable("postId")
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
	public BaseResponse<PostResponses> getLikedPosts(
		@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@RequestParam(name = "cursor", required = false) Long cursor,
		@RequestParam(name = "size", defaultValue = "15") Integer size) {
		PostResponses postResponses = postQueryService.getLikedPosts(customUserDetails.getMember(), size, cursor);
		return BaseResponse.onSuccess(postResponses);
	}

	@GetMapping("/me")
	@Operation(summary = "내가 작성한 게시글 목록 조회 API", description = "내가 작성한 게시글에 대한 목록 조회 API")
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "게시글 목록 조회 성공"
		)
	})
	public BaseResponse<PostResponses> getMyPosts(
		@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@RequestParam(name = "cursor", required = false) Long cursor,
		@RequestParam(name = "size", defaultValue = "15") Integer size) {
		PostResponses postResponses = postQueryService.getMyPosts(customUserDetails.getMember(), size, cursor);
		return BaseResponse.onSuccess(postResponses);
	}

	@PostMapping("/{postId}/reports")
	@Operation(summary = "게시글 신고 API", description = "인증된 유저의 게시글 신고 API")
	@ApiResponses({
		@ApiResponse(responseCode = "COMMON200", description = "게시글 신고 성공"),
		@ApiResponse(
				responseCode = "COMMON401",
				description = "로그인이 필요합니다.",
				content = @Content(
						mediaType = "application/json",
						examples = @ExampleObject(
								value = """
					{
					  "timestamp": "2025-01-26T21:23:51.4515304",
					  "code": "COMMON401",
					  "message": "인증이 필요합니다."
					}
					"""
						)
				)
		),
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
		),
		@ApiResponse(
			responseCode = "POST409",
			description = "이미 신고한 게시물 입니다.",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					value = """
						{
						    "timestamp": "2024-01-13T10:00:00",
						    "isSuccess": "false",
						    "code": "POST409",
						    "message": "이미 신고한 게시물 입니다."
						}
						"""
				)
			)
		)
	})
	@Parameters({
		@Parameter(name = "postId", description = "게시글 아이디, path variable 입니다")
	})
	public BaseResponse<Void> reportPost(
		@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@PathVariable Long postId) {
		postCommandService.reportPost(customUserDetails.getMember(), postId);

		return BaseResponse.onSuccess(null);
	}

	@GetMapping("/{postId}")
	@Operation(summary = "특정 게시글 조회 API", description = "특정 게시글 조회 API")
	@ApiResponses({
			@ApiResponse(responseCode = "COMMON200", description = "게시글 조회 성공"),
			@ApiResponse(
					responseCode = "COMMON401",
					description = "로그인을 하지 않았을 경우",
					content = @Content(
							mediaType = "application/json",
							examples = @ExampleObject(
									value = """
					{
					  "timestamp": "2025-01-26T21:23:51.4515304",
					  "code": "COMMON401",
					  "message": "인증이 필요합니다."
					}
					"""
							)
					)
			),
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
			@Parameter(name = "postId", description = "게시글 아이디, path variable 입니다")
	})
	public BaseResponse<PostResponse> getPost(
			@AuthenticationPrincipal CustomUserDetails customUserDetails,
			@PathVariable Long postId) {
		PostResponse postResponse=postQueryService.getPost(customUserDetails.getMember(), postId);

		return BaseResponse.onSuccess(postResponse);
	}


	@PatchMapping("/{postId}")
	@Operation(summary = "게시글 수정 API", description = "인증된 유저의 게시글 수정 API.\ncontent만 수정하더라도 항상 isPublic의 수정 정보까지 함께 받아오므로 이 점 주의해서 request body 작성해주시면 감사하겠습니다")
	@ApiResponses({
			@ApiResponse(responseCode = "COMMON200", description = "게시글 수정 성공"),
			@ApiResponse(
					responseCode = "COMMON401",
					description = "로그인을 하지 않았을 경우",
					content = @Content(
							mediaType = "application/json",
							examples = @ExampleObject(
									value = """
					{
					  "timestamp": "2025-01-26T21:23:51.4515304",
					  "code": "COMMON401",
					  "message": "인증이 필요합니다."
					}
					"""
							)
					)
			),
			@ApiResponse(
					responseCode = "POST403",
					description = "작성자가 아닌 유저의 요청으로 권한이 없는 경우",
					content = @Content(
							mediaType = "application/json",
							examples = @ExampleObject(
									value = """
          {
            "timestamp": "2025-01-27T20:56:55.7942672",
            "code": "POST403",
            "message": "해당 게시글에 대한 권한이 없습니다."
          }
					"""
							)
					)
			),      
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
			@Parameter(name = "postId", description = "게시글 아이디, path variable 입니다")
	})
	public BaseResponse<Void> updatePost(
			@Valid @RequestBody PostUpdateRequest request,
			@PathVariable(name = "postId") Long postId,
			@AuthenticationPrincipal CustomUserDetails customUserDetails) {
		postCommandService.updatePost(request, postId, customUserDetails.getMember());

		return BaseResponse.onSuccess(null);
	}

	@PatchMapping("/{postId}/public")
	@Operation(summary = "게시글 나만 보기 수정 API", description = "인증된 유저의 게시글 나만 보기 수정 API.")
	@ApiResponses({
			@ApiResponse(responseCode = "COMMON200", description = "게시글 수정 성공"),
			@ApiResponse(
					responseCode = "COMMON401",
					description = "로그인을 하지 않았을 경우",
					content = @Content(
							mediaType = "application/json",
							examples = @ExampleObject(
									value = """
					{
					  "timestamp": "2025-01-26T21:23:51.4515304",
					  "code": "COMMON401",
					  "message": "인증이 필요합니다."
					}
					"""
							)
					)
			),
			@ApiResponse(
					responseCode = "POST403",
					description = "작성자가 아닌 유저의 요청으로 권한이 없는 경우",
					content = @Content(
							mediaType = "application/json",
							examples = @ExampleObject(
									value = """
          					{
            					"timestamp": "2025-01-27T20:56:55.7942672",
            					"code": "POST403",
            					"message": "해당 게시글에 대한 권한이 없습니다."
          					}
					"""
							)
					)
			),
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
			@Parameter(name = "postId", description = "게시글 아이디, path variable 입니다")
	})
	public BaseResponse<Void> updatePostIsPublic(
			@PathVariable(name = "postId") Long postId,
			@AuthenticationPrincipal CustomUserDetails customUserDetails) {

		postCommandService.togglePostPublic(customUserDetails.getMember(), postId);
		return BaseResponse.onSuccess(null);
	}

}
