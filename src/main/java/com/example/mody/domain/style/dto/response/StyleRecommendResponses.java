package com.example.mody.domain.style.dto.response;

import com.example.mody.global.dto.response.CursorPagination;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StyleRecommendResponses {

    @Schema(description = "추천 스타일 리스트")
    private final List<StyleRecommendResponse> styleRecommendResponseList;

    @Schema(description = "커서 기반 페이지네이션")
    private final CursorPagination cursorPagination;

    public static StyleRecommendResponses of(Boolean hasNext, List<StyleRecommendResponse> recommendResponses){
        Long cursor = hasNext ? recommendResponses.getLast().getStyleId() : null;
        return new StyleRecommendResponses(recommendResponses, new CursorPagination(hasNext, cursor));
    }

}
