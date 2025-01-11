package com.example.mody.domain.post.service;

import com.example.mody.domain.backupimage.service.BackupFileService;
import com.example.mody.domain.bodytype.entity.BodyType;
import com.example.mody.domain.bodytype.service.BodyTypeService;
import com.example.mody.domain.exception.BodyTypeException;
import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.post.dto.request.PostCreateRequest;
import com.example.mody.domain.post.entity.Post;
import com.example.mody.domain.post.entity.PostImage;
import com.example.mody.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.example.mody.global.common.exception.code.status.BodyTypeErrorStatus.MEMBER_BODY_TYPE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PostCommandServiceImpl implements PostCommandService{
    private final PostRepository postRepository;

    private final BodyTypeService bodyTypeService;
    private final BackupFileService backupFileService;

    /**
     * 게시글 작성 비즈니스 로직. BodyType은 요청 유저의 가장 마지막 BodyType을 적용함. 유저의 BodyType이 존재하지 않을 경우 예외 발생.
     * @param postCreateRequest
     * @param member
     */
    @Override
    @Transactional
    public void createPost(PostCreateRequest postCreateRequest, Member member) {
        Optional<BodyType> optionalBodyType = bodyTypeService.findLastBodyType(member);

        BodyType bodyType = optionalBodyType.orElseThrow(()-> new BodyTypeException(MEMBER_BODY_TYPE_NOT_FOUND));

        Post post = new Post(member,
                bodyType,
                postCreateRequest.getContent(),
                postCreateRequest.getIsPublic());

        postCreateRequest.getFiles().forEach(file->{
            PostImage postImage = new PostImage(post, file.getS3Url());
            post.getImages().add(postImage);
            backupFileService.saveBackupFile(file); // 백업파일 저장
        });

        postRepository.save(post);
    }
}
