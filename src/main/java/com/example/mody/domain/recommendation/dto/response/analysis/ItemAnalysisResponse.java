package com.example.mody.domain.recommendation.dto.response.analysis;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "패션 아이템 추천 Gpt 응답")
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ItemAnalysisResponse {

    private String nickName;

    @Schema(
            description = "패션 아이템 이름",
            example = "레더 자켓"
    )
    private String item;

    @Schema(
            description = "설명",
            example = "네추럴 체형의 강인한 이미지를 살리면서도 힙/스트릿 및 빈티지 스타일과 잘 어우러지는 레더 자켓은 심세원님의 골격과 체형에 적합합니다. 어깨 라인을 강조하고, 시크하면서도 섹시한 이미지를 표현하는 데 효과적입니다. 슬림핏 혹은 크롭 스타일의 레더 자켓은 긴 다리와 상체 비율을 살려주는 아이템입니다."
    )
    private String description;

    @Schema(
            description = "이미지 url",
            example = "https://i.pinimg.com/236x/32/87/f8/3287f86756200b3c8d9d28181aaddeae.jpg"
    )
    private String imageUrl;

    public ItemAnalysisResponse from(String imageUrl) {
        return this.toBuilder()
                .imageUrl(imageUrl)
                .build();
    }
}
