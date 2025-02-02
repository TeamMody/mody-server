package com.example.mody.domain.fashionItem.dto.response;

import com.example.mody.domain.fashionItem.entity.FashionItem;
import com.example.mody.global.dto.response.CursorPagination;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Schema(description = "패션 아이템 추천 응답 DTO")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ItemsResponse {

    @Schema(
            description = "회원 닉네임",
            example = "영희"
    )
    private String nickName;

    @Schema(description = "패션 아이템 추천 리스트")
    private List<ItemRecommendation> fashionItemRecommendations;

    //여러 추천 결과 생성 메서드
    public static ItemsResponse of(String nickName, List<FashionItem> fashionItems) {
        List<ItemRecommendation> recommendations = fashionItems.stream()
                .map(ItemRecommendation::from)
                .collect(Collectors.toList());

        return ItemsResponse.builder()
                .nickName(nickName)
                .fashionItemRecommendations(recommendations)
                .build();
    }

    @Schema(description = "사용자의 아이템 추천 정보")
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class ItemRecommendation {

        @Schema(
                description = "패션 아이템 아이디",
                example = "1"
        )
        private Long id;

        @Schema(
                description = "패션 아이템 이름",
                example = "레더 자켓"
        )
        public String item;

        @Schema(
                description = "설명",
                example = "네추럴 체형의 강인한 이미지를 살리면서도 힙/스트릿 및 빈티지 스타일과 잘 어우러지는 레더 자켓은 심세원님의 골격과 체형에 적합합니다. 어깨 라인을 강조하고, 시크하면서도 섹시한 이미지를 표현하는 데 효과적입니다. 슬림핏 혹은 크롭 스타일의 레더 자켓은 긴 다리와 상체 비율을 살려주는 아이템입니다."
        )
        private String description;

        @Schema(
                description = "이미지 url",
                example = "https://i.pinimg.com/736x/72/ea/9e/72ea9eb6a5c7610b90a37aef9a022e12.jpg"
        )
        private String imageUrl;

        @Schema(
                description = "좋아요 여부",
                example = "true"
        )
        private boolean isLiked;

        public static ItemRecommendation from(FashionItem fashionItem) {
            return ItemRecommendation.builder()
                    .id(fashionItem.getId())
                    .item(fashionItem.getItem())
                    .description(fashionItem.getDescription())
                    .imageUrl(fashionItem.getImageUrl())
                    .isLiked(fashionItem.isLiked())
                    .build();
        }
    }
}
