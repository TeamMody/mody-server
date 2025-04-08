package com.example.mody.domain.recommendation.dto.request;

import com.example.mody.global.common.exception.annotation.IsEmptyList;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Schema(description = "특정 상황에 어울리는 패션 추천 요청 DTO")
@Data
@Getter
public class OccasionRecommendRequest {

    @Schema(description = "특정 상황", example = "결혼식")
    private String occasion;

    @Schema(
            description = "선호하는 스타일",
            example = "[\"힙/스트릿\",\"빈티지\"]"
    )
    @NotNull(message = "선호하는 스타일은 필수 항목입니다.")
    @IsEmptyList(message = "선호하는 스타일 목록은 비어 있을 수 없습니다.")
    private List<String> preferredStyles;
}
