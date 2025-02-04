package com.example.mody.domain.recommendation.dto.response;

import com.example.mody.global.dto.response.CursorPagination;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Schema(description = "추천 결과 응답")
@Getter
@Builder
@AllArgsConstructor
public class RecommendResponses {

    @Schema(description = "추천 스타일 리스트")
    private final List<RecommendResponse> recommendResponseList;

    @Schema(description = "커서 기반 페이지네이션")
    private final CursorPagination cursorPagination;

    public static RecommendResponses of(Boolean hasNext, List<RecommendResponse> recommendResponses){
        Long cursor = hasNext ? recommendResponses.getLast().getRecommendationId() : null;
        return new RecommendResponses(recommendResponses, new CursorPagination(hasNext, cursor));
    }
}
