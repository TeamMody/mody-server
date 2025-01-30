package com.example.mody.domain.fashionItem.service;

import com.example.mody.domain.bodytype.entity.mapping.MemberBodyType;
import com.example.mody.domain.bodytype.repository.MemberBodyTypeRepository;
import com.example.mody.domain.chatgpt.service.ChatGptService;
import com.example.mody.domain.exception.BodyTypeException;
import com.example.mody.domain.fashionItem.dto.request.FashionItemRequest;
import com.example.mody.domain.fashionItem.dto.response.FashionItemRecommendResponse;
import com.example.mody.domain.fashionItem.entity.FashionItem;
import com.example.mody.domain.fashionItem.repository.FashionItemRepository;
import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.style.dto.BodyTypeDTO;
import com.example.mody.global.common.exception.code.status.BodyTypeErrorStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FashionItemCommandServiceImpl implements FashionItemCommandService{

    private final MemberBodyTypeRepository memberBodyTypeRepository;
    private final ObjectMapper objectMapper;
    private final ChatGptService chatGptService;
    private final FashionItemRepository fashionItemRepository;

    @Override
    public FashionItemRecommendResponse recommendItem(FashionItemRequest request, Member member) {

        //현재 유저의 bodyType 정보를 받아오기
        MemberBodyType latestBodyType = memberBodyTypeRepository.findTopByMemberOrderByCreatedAt(member)
                .orElseThrow(() -> new BodyTypeException(BodyTypeErrorStatus.MEMBER_BODY_TYPE_NOT_FOUND));

        //아이템 추천을 위한 gpt 프롬프트에 넘길 체형분석 데이터 구성
        BodyTypeDTO bodyTypeDTO = new BodyTypeDTO(
                latestBodyType.getBody(),
                latestBodyType.getBodyType().getName()
        );

        //bodyType 데이터를 String 형태로 변환 (gpt로 넘겨주기 위해서)
        String bodyType = convertBodyTypeToJson(bodyTypeDTO);

        FashionItemRecommendResponse response = chatGptService.recommendGptItem(request, bodyType);

        FashionItem fashionItem = FashionItem.builder()
                .item(response.getItem())
                .description(response.getDescription())
                .imageUrl(response.getImageUrl())
                .member(member)
                .build();

        fashionItemRepository.save(fashionItem);

        return response;
    }

    private String convertBodyTypeToJson(BodyTypeDTO bodyType) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(bodyType);
        } catch (JsonProcessingException e) {
            throw new BodyTypeException(BodyTypeErrorStatus.JSON_PARSING_ERROR);
        }
    }
}
