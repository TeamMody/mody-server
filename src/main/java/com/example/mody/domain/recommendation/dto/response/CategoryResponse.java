package com.example.mody.domain.recommendation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Schema(description = "스타일 카테고리 조회 DTO")
@Getter
@Data
@AllArgsConstructor
public class CategoryResponse {

    @Schema(
            description = "스타일 카테고리",
            example = "[\n" +
                    "    \"캐주얼\",\n" +
                    "    \"컴퍼스룩\",\n" +
                    "    \"스트릿\",\n" +
                    "    \"컴퍼스룩\",\n" +
                    "    \"스트릿\"],"
    )
    private List<String> styleCategories;

    @Schema(
            description = "보여지고 싶은 이미지 카테고리",
            example = "[\n" +
                    "    \"지적인\",\n" +
                    "    \"섹시한\",\n" +
                    "    \"귀여운\",\n" +
                    "    \"지적인\",\n" +
                    "    ]"
    )
    private List<String> appealCategories;
}
