package com.example.mody.domain.member.service;

import com.example.mody.domain.member.dto.response.MemberProfileResponse;
import com.example.mody.domain.member.entity.Member;

public interface MemberQueryService {
    Member findMemberById(Long memberId);
    MemberProfileResponse getMyProfile(Member member);
}
