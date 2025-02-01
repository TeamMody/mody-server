package com.example.mody.domain.style.service;

import com.example.mody.domain.bodytype.entity.mapping.MemberBodyType;
import com.example.mody.domain.bodytype.repository.MemberBodyTypeRepository;
import com.example.mody.domain.chatgpt.service.ChatGptService;
import com.example.mody.domain.exception.BodyTypeException;
import com.example.mody.domain.fashionItem.dto.response.ItemLikeResponse;
import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.style.dto.BodyTypeDto;
import com.example.mody.domain.style.dto.request.StyleRecommendRequest;
import com.example.mody.domain.style.dto.response.StyleRecommendResponse;
import com.example.mody.domain.style.dto.response.StyleRecommendation;
import com.example.mody.domain.style.entity.Style;
import com.example.mody.domain.style.entity.mapping.MemberStyleLike;
import com.example.mody.domain.style.repository.MemberStyleLikeRepository;
import com.example.mody.domain.style.repository.StyleRepository;
import com.example.mody.global.common.exception.code.status.BodyTypeErrorStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class StyleCommandServiceImpl implements StyleCommandService{

    private final StyleRepository styleRepository;
    private final ChatGptService chatGptService;
    private final MemberBodyTypeRepository memberBodyTypeRepository;
    private final ObjectMapper objectMapper;
    private final StyleQueryService styleQueryService;
    private final MemberStyleLikeRepository memberStyleLikeRepository;

    //gptService로 답변 받아오고 데이터를 디비에 저장
    @Override
    public StyleRecommendResponse recommendStyle(StyleRecommendRequest request, Member member) {

        //현재 유저의 bodyType 정보를 받아오기
        MemberBodyType latestBodyType = memberBodyTypeRepository.findTopByMemberOrderByCreatedAt(member)
                .orElseThrow(() -> new BodyTypeException(BodyTypeErrorStatus.MEMBER_BODY_TYPE_NOT_FOUND));

        //style 추천을 위한 gpt 프롬프트에 넘길 체형분석 데이터 구성
        BodyTypeDto bodyTypeDto = BodyTypeDto.of(
                latestBodyType.getBody(),
                latestBodyType.getBodyType().getName()
        );

        //bodyType 데이터를 String 형태로 변환 (gpt로 넘겨주기 위해서)
        String bodyType = convertBodyTypeToJson(bodyTypeDto);

        //ChatGptService를 호출하여 스타일 추천 데이터를 가져옴
        StyleRecommendation recommendation = chatGptService.recommendGptStyle(request, bodyType);


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

        //스타일 추천 결과 응답 생성
        StyleRecommendResponse response = StyleRecommendResponse.of(member, style, false);

        return response;
    }

    @Override
    public ItemLikeResponse toggleLike(Long styleId, Member member) {
        Style style = styleQueryService.findById(styleId);
        Optional<MemberStyleLike> existingLike = memberStyleLikeRepository.findTopByMemberAndStyle(member, style);

        if(existingLike.isPresent()){
            deleteLike(existingLike.get(), style);
        }else{
            addLike(member, style);
        }

        return ItemLikeResponse.builder()
                .isLiked(!existingLike.isPresent())
                .itemId(styleId)
                .build();
    }

    private void addLike(Member member, Style style){
        MemberStyleLike newMemberStyleLike = new MemberStyleLike(member, style);
        memberStyleLikeRepository.save(newMemberStyleLike);
        increaseLikeCount(style);
    }

    private void increaseLikeCount(Style style){
        style.increaseLikeCount();
        style.getMember()
                .increaseLikeCount();
    }

    private void deleteLike(MemberStyleLike memberStyleLike, Style style){
        memberStyleLikeRepository.delete(memberStyleLike);
        decreaseLikeCount(style);
    }

    private void decreaseLikeCount(Style style){
        style.decreaseLikeCount();
        style.getMember()
                .decreaseLikeCount();
    }


    //bodyType 데이터 String으로 바꾸기
    private String convertBodyTypeToJson(BodyTypeDto bodyType) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(bodyType);
        } catch (JsonProcessingException e) {
            throw new BodyTypeException(BodyTypeErrorStatus.JSON_PARSING_ERROR);
        }
    }
}
