package com.example.mody.domain.recommendation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "추천 좋아요 응답")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RecommendLikeResponse {

    @Schema(
            description = "멤버 아이디",
            example = "1"
    )
    private Long memberId;

    @Schema(
            description = "추천 아이디",
            example = "1"
    )
    private Long recommendId;

    @Schema(
            description = "좋아요 여부",
            example = "true"
    )
    private boolean isLiked;

    public void setLiked(boolean liked) {
        isLiked = liked;
    }
}
