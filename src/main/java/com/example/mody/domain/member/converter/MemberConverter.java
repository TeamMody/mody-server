package com.example.mody.domain.member.converter;

import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.member.enums.Gender;
import com.example.mody.domain.member.web.dto.MemberRequestDTO;
import com.example.mody.domain.member.web.dto.MemberResponseDTO;

import java.time.LocalDateTime;

public class MemberConverter {

    public static Member toMember(MemberRequestDTO.JoinDto request) {
        Gender gender = null;
        int genderValue = request.getGender();
        if (genderValue == 1) {
            gender = Gender.MALE;
        } else if (genderValue == 2) {
            gender = Gender.FEAMLE;
        } else {
            gender = Gender.NONE;
        }

        return Member.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .nickname(request.getNickname())
                .gender(gender)
                .height(request.getHeight())
                .build();
    }

    public static MemberResponseDTO.JoinResultDto toJoinResultDto(Member member) {
        return MemberResponseDTO.JoinResultDto.builder()
                .memberId(member.getId())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
