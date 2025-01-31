package com.example.mody.domain.style.dto;

import lombok.*;

/**
 * 스타일 추천에 필요한 사용자 정보를 담을 DTO
 * - 추후 변경 예정
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BodyTypeDto {

    //gpt를 통한 체형 분석 내용
    private String body;

    //체형 타입 이름
    private String bodyTypeName;

    public static BodyTypeDto of(String body, String bodyTypeName) {
        return BodyTypeDto.builder()
                .body(body)
                .bodyTypeName(bodyTypeName)
                .build();
    }
}
