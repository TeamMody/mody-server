package com.example.mody.domain.bodytype.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 체형 분석 요청 DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class BodyTypeAnalysisRequest {
    @Schema(description = "체형 분석 질문에 대한 답변", example = "목 두께가 얇고 상대적으로 긴 편이다. "
            + "쇄골이 굵고 단단하며 뼈와 힘줄이 돋보인다. "
            + "보송보송하고 관절과 힘줄이 부각된다. "
            + "다리 길이가 상대적으로 긴 편이다. "
            + "허리가 굴곡이 없는 편이다. "
            + "어깨의 뼈가 크고 넓은 편이다. "
            + "엉덩이가 입체적이며 탄탄하다. "
            + "어깨선 및 쇄골뼈가 부각되어 있다. "
            + "탄탄하고 근육이 붙어있다. "
            + "팔, 가슴, 배 등 상체 위주로 살이 찐다. "
            + "보통이다.")
    private String answer;
}