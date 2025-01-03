package com.example.mody.domain.member.service;

import com.example.mody.domain.member.converter.MemberConverter;
import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.member.repository.MemberRepository;
import com.example.mody.domain.member.web.dto.MemberRequestDTO;
import com.example.mody.global.common.exception.RestApiException;
import com.example.mody.global.common.exception.code.status.AuthErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberCommandServiceImpl implements MemberCommandService{

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Member joinMember(MemberRequestDTO.JoinDto request) {

        Member newMember = MemberConverter.toMember(request);
        newMember.encodePassword(passwordEncoder.encode(request.getPassword()));
        return memberRepository.save(newMember);
    }

    @Override
    @Transactional(readOnly = true)
    public Member authenticate(String email, String rawPassword) {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RestApiException(AuthErrorStatus.INVALID_ID_TOKEN));

        if (!passwordEncoder.matches(rawPassword, member.getPassword())) {
            throw new RestApiException(AuthErrorStatus.INVALID_ACCESS_TOKEN);
        }
        return member;
    }
}
