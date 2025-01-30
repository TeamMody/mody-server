package com.example.mody.domain.fashionItem.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "패션 아이템 추천 API 응답")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ItemRecommendResponse {

    @Schema(
            description = "회원 닉네임",
            example = "영희"
    )
    private String nickName;

    private ItemGptResponse itemGptResponse;
}
