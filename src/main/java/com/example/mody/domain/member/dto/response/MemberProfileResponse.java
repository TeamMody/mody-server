package com.example.mody.domain.member.dto.response;

import com.example.mody.domain.bodytype.entity.BodyType;
import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.member.enums.Gender;
import com.example.mody.domain.member.enums.LoginType;
import com.example.mody.domain.member.enums.Role;
import com.example.mody.domain.member.enums.Status;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Optional;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberProfileResponse {
    private final Long id;
    private final String email;
    private final String bodyType;
    private final String provider;
    private final String nickname;
    private final Integer likeCount;
    private final Long inspectedBodyTypeCount;
    private final String profileImageUrl;
    private final LocalDate birthDate;
    private final Gender gender;
    private final Integer height;
    private final Status status;
    private final Role role;
    private final LoginType loginType;

    public static MemberProfileResponse of(Member member, Optional<BodyType> bodyType, Long inspectedBodyTypeCount){
        return MemberProfileResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .bodyType(bodyType
                        .map(BodyType::getName)
                        .orElse(null))
                .provider(member.getProvider())
                .nickname(member.getNickname())
                .likeCount(member.getLikeCount())
                .inspectedBodyTypeCount(inspectedBodyTypeCount)
                .profileImageUrl(member.getProfileImageUrl())
                .birthDate(member.getBirthDate())
                .gender(member.getGender())
                .height(member.getHeight())
                .status(member.getStatus())
                .role(member.getRole())
                .loginType(member.getLoginType())
                .build();
    }

}
