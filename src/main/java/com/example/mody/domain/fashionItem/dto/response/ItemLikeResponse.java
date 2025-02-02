package com.example.mody.domain.fashionItem.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "아이템 좋아요 응답")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ItemLikeResponse {

    @Schema(
            description = "패션 아이템 아이디",
            example = "1"
    )
    private Long itemId;

    @Schema(
            description = "좋아요 여부",
            example = "true"
    )
    private boolean isLiked;
}
