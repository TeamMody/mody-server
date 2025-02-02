package com.example.mody.domain.fashionItem.dto.request;

import com.example.mody.global.common.exception.annotation.IsEmptyList;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Schema(description = "패션 아이템 추천 요청 DTO")
@Getter
public class FashionItemRequest {

    @Schema(
            description = "선호하는 스타일",
            example = "[\"힙/스트릿\",\"빈티지\"]"
    )
    @NotNull(message = "선호하는 스타일은 필수 항목입니다.")
    @IsEmptyList(message = "선호하는 스타일 목록은 비어 있을 수 없습니다.")
    private List<String> preferredStyles;

    @Schema(
            description = "사용자가 선호하지 않는 스타일 목록 (예: 포멀 등)",
            example = "[\"페미닌\", \"러블리\"]"
    )
    @NotNull(message = "선호하지 않는 스타일은 필수 항목입니다.")
    @IsEmptyList(message = "선호하지 않는 스타일 목록은 비어 있을 수 없습니다.")
    private List<String> dislikedStyles;

    @Schema(
            description = "사용자가 보여주고 싶은 이미지 설명 (예: 세련되고 자유로운 이미지)",
            example = "[\"섹시한\", \"시크한\"]"
    )
    @NotNull(message = "보여주고 싶은 이미지는 필수 항목입니다.")
    @IsEmptyList(message = "보여주고 싶은 이미지 목록은 비어 있을 수 없습니다.")
    private List<String> appealedImage;
}
