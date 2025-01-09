package com.example.mody.domain.member.converter;

import com.example.mody.domain.auth.dto.request.MemberJoinRequest;
import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.member.enums.Role;
import com.example.mody.domain.member.enums.Status;

public class MemberConverter {

    public static Member toMember(MemberJoinRequest request, String email) {

        return Member.builder()
                .email(email)
                .password(request.getPassword())
                .nickname(request.getNickname())
                .gender(request.getGender())
                .height(request.getHeight())
                .status(Status.ACTIVE)
                .role(Role.ROLE_USER)
                .isRegistrationCompleted(true)
                .build();
    }
}
