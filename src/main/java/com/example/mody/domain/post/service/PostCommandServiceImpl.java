package com.example.mody.domain.post.service;

import static com.example.mody.global.common.exception.code.status.BodyTypeErrorStatus.*;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.mody.domain.backupimage.service.BackupFileService;
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

	private final BodyTypeService bodyTypeService;
	private final BackupFileService backupFileService;

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

		postCreateRequest.getFiles().forEach(file -> {
			PostImage postImage = new PostImage(post, file.getS3Url());
			post.getImages().add(postImage);
			backupFileService.saveBackupFile(file); // 백업파일 저장
		});

		postRepository.save(post);
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
		} else {
			// 좋아요가 없다면 새로 생성합니다.
			MemberPostLike postLike = MemberPostLike.createMemberPostLike(member, post);
			postLikeRepository.save(postLike);
			// 게시글의 좋아요 수를 증가시킵니다.
			post.increaseLikeCount();
		}

		// 게시글 엔티티는 @Transactional에 의해 자동으로 저장됩니다.
	}
}
