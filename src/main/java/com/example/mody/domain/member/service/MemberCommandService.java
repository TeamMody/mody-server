package com.example.mody.domain.member.service;

import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.member.web.dto.MemberRequestDTO;

public interface MemberCommandService {

    public Member joinMember(MemberRequestDTO.JoinDto request);

    Member authenticate(String email, String rawPassword);
}
