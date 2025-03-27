package com.example.mody.domain.member.service;

import com.example.mody.domain.bodytype.entity.BodyType;
import com.example.mody.domain.bodytype.repository.MemberBodyTypeRepository;
import com.example.mody.domain.bodytype.service.memberbodytype.MemberBodyTypeQueryService;
import com.example.mody.domain.exception.MemberException;
import com.example.mody.domain.member.dto.response.MemberProfileResponse;
import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.member.repository.MemberRepository;
import com.example.mody.global.common.exception.code.status.MemberErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberQueryServiceImpl implements MemberQueryService{
    private final MemberBodyTypeQueryService memberBodyTypeQueryService;

    private final MemberRepository memberRepository;
    private final MemberBodyTypeRepository memberBodyTypeRepository;

    @Override
    public Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorStatus.MEMBER_NOT_FOUND));
    }

    @Override
    public MemberProfileResponse getMyProfile(Member member) {
        Optional<BodyType> bodyType = memberBodyTypeQueryService.findLastBodyType(member);
        Long bodyTypeCount = memberBodyTypeRepository.countAllByMember(member);
        return MemberProfileResponse.of(member, bodyType, bodyTypeCount);
    }

}
