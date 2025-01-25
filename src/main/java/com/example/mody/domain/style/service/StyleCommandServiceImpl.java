package com.example.mody.domain.style.service;

import com.example.mody.domain.bodytype.entity.mapping.MemberBodyType;
import com.example.mody.domain.bodytype.repository.MemberBodyTypeRepository;
import com.example.mody.domain.chatgpt.service.ChatGptService;
import com.example.mody.domain.exception.BodyTypeException;
import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.style.dto.BodyTypeDTO;
import com.example.mody.domain.style.dto.request.StyleRecommendRequest;
import com.example.mody.domain.style.dto.response.StyleRecommendResponse;
import com.example.mody.domain.style.entity.Style;
import com.example.mody.domain.style.repository.StyleRepository;
import com.example.mody.global.common.exception.code.status.BodyTypeErrorStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class StyleCommandServiceImpl implements StyleCommandService{

    private final StyleRepository styleRepository;
    private final ChatGptService chapGptService;
    private final MemberBodyTypeRepository memberBodyTypeRepository;
    private final ObjectMapper objectMapper;

    //gptService로 답변 받아오고 데이터를 디비에 저장
    @Override
    public StyleRecommendResponse recommendStyle(StyleRecommendRequest request, Member member) {

        //현재 유저의 bodyType 정보를 받아오기
        MemberBodyType lastestBodyType = memberBodyTypeRepository.findTopByMemberOrderByCreatedAt(member)
                .orElseThrow(() -> new BodyTypeException(BodyTypeErrorStatus.MEMBER_BODY_TYPE_NOT_FOUND));

        //style 추천을 위한 gpt 프롬프트에 넘길 체형분석 데이터 구성
        BodyTypeDTO bodyTypeDTO = new BodyTypeDTO(
                lastestBodyType.getBody(),
                lastestBodyType.getBodyType().getName()
        );

        //bodyType 데이터를 String 형태로 변환 (gpt로 넘겨주기 위해서)
        String bodyType = convertBodyTypeToJson(bodyTypeDTO);

        //ChatGptService를 호출하여 스타일 추천 데이터를 가져옴
        StyleRecommendResponse.StyleRecommendation recommendation = chapGptService.recommendGptStyle(request, bodyType);

        //스타일 추천 결과 응답 생성
        StyleRecommendResponse response = StyleRecommendResponse.of(member.getNickname(), recommendation);

        //데이터 저장
        Style style = new Style(
                recommendation.getRecommendedStyle(),
                recommendation.getIntroduction(),
                recommendation.getStyleDirection(),
                recommendation.getPracticalStylingTips(),
                member,
                recommendation.getImageUrl()
        );

        styleRepository.save(style);

        return response;
    }

    //bodyType 데이터 String으로 바꾸기
    private String convertBodyTypeToJson(BodyTypeDTO bodyType) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(bodyType);
        } catch (JsonProcessingException e) {
            throw new BodyTypeException(BodyTypeErrorStatus.JSON_PARSING_ERROR);
        }
    }
}
