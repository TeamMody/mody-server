package com.example.mody.domain.post.service;

import static com.example.mody.global.common.exception.code.status.BodyTypeErrorStatus.*;
import static com.example.mody.global.common.exception.code.status.PostErrorStatus.*;

import java.util.List;
import java.util.Optional;


import com.example.mody.domain.file.repository.BackupFileRepository;
import com.example.mody.domain.file.service.FileService;
import com.example.mody.domain.post.dto.request.PostUpdateRequest;
import com.example.mody.domain.post.entity.mapping.PostReport;
import com.example.mody.domain.post.repository.PostReportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.mody.domain.bodytype.entity.BodyType;
import com.example.mody.domain.bodytype.service.BodyTypeService;
import com.example.mody.domain.exception.BodyTypeException;
import com.example.mody.domain.exception.MemberException;
import com.example.mody.domain.exception.PostException;
import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.member.repository.MemberRepository;
import com.example.mody.domain.post.dto.request.PostCreateRequest;
import com.example.mody.domain.post.entity.Post;
import com.example.mody.domain.post.entity.PostImage;
import com.example.mody.domain.post.entity.mapping.MemberPostLike;
import com.example.mody.domain.post.repository.MemberPostLikeRepository;
import com.example.mody.domain.post.repository.PostImageRepository;
import com.example.mody.domain.post.repository.PostRepository;
import com.example.mody.global.common.exception.code.status.MemberErrorStatus;
import com.example.mody.global.common.exception.code.status.PostErrorStatus;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PostCommandServiceImpl implements PostCommandService {
	private final PostRepository postRepository;
	private final MemberRepository memberRepository;
	private final MemberPostLikeRepository postLikeRepository;
	private final PostImageRepository postImageRepository;
	private final BackupFileRepository backupFileRepository;
	private final PostReportRepository postReportRepository;

	private final BodyTypeService bodyTypeService;
	private final FileService fileService;

	/**
	 * 게시글 작성 비즈니스 로직. BodyType은 요청 유저의 가장 마지막 BodyType을 적용함. 유저의 BodyType이 존재하지 않을 경우 예외 발생.
	 * @param postCreateRequest
	 * @param member
	 */
	@Override
	public void createPost(PostCreateRequest postCreateRequest, Member member) {
		Optional<BodyType> optionalBodyType = bodyTypeService.findLastBodyType(member);

		BodyType bodyType = optionalBodyType.orElseThrow(() -> new BodyTypeException(MEMBER_BODY_TYPE_NOT_FOUND));

		Post post = new Post(member,
			bodyType,
			postCreateRequest.getContent(),
			postCreateRequest.getIsPublic());

		postCreateRequest.getS3Urls().forEach(s3Url -> {
			PostImage postImage = new PostImage(post, s3Url);
			post.getImages().add(postImage);
		});

		postRepository.save(post);
	}

	@Override
	@Transactional
	public void deletePost(Long postId, Member member) {
		// 게시글 조회 및 존재 여부 확인
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new PostException(POST_NOT_FOUND));

		checkAuthorization(member, post);
		delete(post);
	}

	@Override
	public void togglePostLike(Long myId, Long postId) {

		// 게시글과 사용자 존재 여부는 @ExistsPost와 @ExistsMember 어노테이션으로 이미 검증되었습니다.
		// 하지만 실제 엔티티가 필요하므로 조회합니다.
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new PostException(PostErrorStatus.POST_NOT_FOUND));

		Member member = memberRepository.findById(myId)
			.orElseThrow(() -> new MemberException(MemberErrorStatus.MEMBER_NOT_FOUND));

		// 이미 좋아요를 눌렀는지 확인합니다.
		Optional<MemberPostLike> existingLike = postLikeRepository.findByPostAndMember(post, member);

		if (existingLike.isPresent()) {
			// 이미 좋아요가 있다면 취소(삭제)합니다.
			postLikeRepository.delete(existingLike.get());
			// 게시글의 좋아요 수를 감소시킵니다.
			post.decreaseLikeCount();
			post.getMember().decreaseLikeCount();
		} else {
			// 좋아요가 없다면 새로 생성합니다.
			MemberPostLike postLike = MemberPostLike.createMemberPostLike(member, post);
			postLikeRepository.save(postLike);
			// 게시글의 좋아요 수를 증가시킵니다.
			post.increaseLikeCount();
			post.getMember().increaseLikeCount();
		}

		// 게시글 엔티티는 @Transactional에 의해 자동으로 저장됩니다.
	}

	@Override
	public void reportPost(Member member, Long postId) {

		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new PostException(POST_NOT_FOUND));

		// 이미 신고를 했는지 확인
		if (postReportRepository.existsByMemberAndPost(member, post)) {
			throw new PostException(POST_ALREADY_REPORT);
		}

		// 신고 기록 저장
		PostReport postReport = new PostReport(member, post);
		postReportRepository.save(postReport);

		// 신고 횟수 증가
		post.getMember().increaseReportCount();
		post.increaseReportCount();

		// 신고 횟수가 10회 이상이면 게시글 삭제
		if (post.getReportCount() >= 10) {
			postReportRepository.deleteAllByPost(post);
			delete(post);
		}
	}

	@Override
	public void updatePost(PostUpdateRequest request, Long postId, Member member){
		Post post = postRepository.findById(postId)
				.orElseThrow(() -> new PostException(POST_NOT_FOUND));
		checkAuthorization(member, post);
		post.updatePost(request.getContent(), request.getIsPublic());
	}

	@Transactional
	protected void delete(Post post) {
		List<String> postImageUrls = postImageRepository.findPostImageUrlByPostId(post.getId());
		fileService.deleteByS3Urls(postImageUrls);
		postRepository.deleteById(post.getId());
	}

	private void checkAuthorization(Member member, Post post){
		if(! member.equals(post.getMember())){
			throw new PostException(POST_FORBIDDEN);
		}
	}
}
