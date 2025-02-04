package com.example.mody.domain.recommendation.dto.response;

import com.example.mody.domain.recommendation.enums.RecommendType;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "추천 응답")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecommendResponse {

    @Schema(
            description = "회원 아이디",
            example = "1"
    )
    private Long memberId;

    @Schema(
            description = "회원 닉네임",
            example = "영희"
    )
    private String nickname;

    @Schema(description = "추천 아이디")
    private Long recommendationId;

    @Schema(description = "추천 타입")
    private RecommendType recommendType;

    @Schema(description = "좋아요 눌렀는지 여부")
    private boolean isLiked = false;

    private String title;

    private String content;

    private String imageUrl;

    public static RecommendResponse of(Long memberId, String nickname, Long recommendationId, RecommendType recommendType, String title, String content, String imageUrl) {
        return RecommendResponse.builder()
                .memberId(memberId)
                .nickname(nickname)
                .recommendationId(recommendationId)
                .recommendType(recommendType)
                .title(title)
                .content(content)
                .imageUrl(imageUrl)
                .build();
    }

    @QueryProjection
    public RecommendResponse(Long memberId, String nickname, Long recommendationId, RecommendType recommendType, boolean isLiked, String title, String content, String imageUrl) {
        this.memberId = memberId;
        this.nickname = nickname;
        this.recommendationId = recommendationId;
        this.recommendType = recommendType;
        this.isLiked = isLiked;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
    }
}
