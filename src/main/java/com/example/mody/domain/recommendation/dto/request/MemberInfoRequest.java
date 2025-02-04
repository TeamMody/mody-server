package com.example.mody.domain.recommendation.dto.request;

import com.example.mody.domain.member.enums.Gender;
import lombok.*;

/**
 * 추천에 필요한 사용자 정보를 담을 DTO
 * - 추후 변경 예정
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberInfoRequest {
    private String nickName;

    private Gender gender;

    //gpt를 통한 체형 분석 내용
    private String body;

    //체형 타입 이름
    private String bodyTypeName;

    public static MemberInfoRequest of(String nickName, Gender gender,String body, String bodyTypeName) {
        return MemberInfoRequest.builder()
                .nickName(nickName)
                .gender(gender)
                .body(body)
                .bodyTypeName(bodyTypeName)
                .build();
    }
}
