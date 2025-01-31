package com.example.mody.domain.fashionItem.service;

import com.example.mody.domain.bodytype.entity.mapping.MemberBodyType;
import com.example.mody.domain.bodytype.repository.MemberBodyTypeRepository;
import com.example.mody.domain.chatgpt.service.ChatGptService;
import com.example.mody.domain.exception.BodyTypeException;
import com.example.mody.domain.fashionItem.dto.request.FashionItemRequest;
import com.example.mody.domain.fashionItem.dto.response.ItemGptResponse;
import com.example.mody.domain.fashionItem.dto.response.ItemLikeResponse;
import com.example.mody.domain.fashionItem.dto.response.ItemRecommendResponse;
import com.example.mody.domain.fashionItem.entity.FashionItem;
import com.example.mody.domain.fashionItem.repository.ItemRepository;
import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.style.dto.BodyTypeDto;
import com.example.mody.global.common.exception.RestApiException;
import com.example.mody.global.common.exception.code.status.BodyTypeErrorStatus;
import com.example.mody.global.common.exception.code.status.FashionItemErrorStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemCommandServiceImpl implements ItemCommandService {

    private final MemberBodyTypeRepository memberBodyTypeRepository;
    private final ObjectMapper objectMapper;
    private final ChatGptService chatGptService;
    private final ItemRepository itemRepository;

    @Override
    public ItemRecommendResponse recommendItem(FashionItemRequest request, Member member) {

        //현재 유저의 bodyType 정보를 받아오기
        MemberBodyType latestBodyType = memberBodyTypeRepository.findTopByMemberOrderByCreatedAt(member)
                .orElseThrow(() -> new BodyTypeException(BodyTypeErrorStatus.MEMBER_BODY_TYPE_NOT_FOUND));

        //아이템 추천을 위한 gpt 프롬프트에 넘길 체형분석 데이터 구성
        BodyTypeDto bodyTypeDto = BodyTypeDto.of(
                latestBodyType.getBody(),
                latestBodyType.getBodyType().getName()
        );

        //bodyType 데이터를 String 형태로 변환 (gpt로 넘겨주기 위해서)
        String bodyType = convertBodyTypeToJson(bodyTypeDto);

        ItemGptResponse recommendation = chatGptService.recommendGptItem(request, bodyType);

        FashionItem fashionItem = FashionItem.builder()
                .item(recommendation.getItem())
                .description(recommendation.getDescription())
                .imageUrl(recommendation.getImageUrl())
                .member(member)
                .build();

        itemRepository.save(fashionItem);

        ItemRecommendResponse response = ItemRecommendResponse.builder()
                .nickName(member.getNickname())
                .itemGptResponse(recommendation)
                .build();

        return response;
    }

    @Override
    public ItemLikeResponse toggleLike(Long fashionItemId, Long memberId) {
        FashionItem fashionItem = itemRepository.findByIdAndMemberId(fashionItemId, memberId)
                .orElseThrow(() -> new RestApiException(FashionItemErrorStatus.ITEM_NOT_FOUND));

        fashionItem.toggleLike();

        ItemLikeResponse response = ItemLikeResponse.builder()
                .itemId(fashionItem.getId())
                .isLiked(fashionItem.isLiked())
                .build();

        return response;
    }

    private String convertBodyTypeToJson(BodyTypeDto bodyType) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(bodyType);
        } catch (JsonProcessingException e) {
            throw new BodyTypeException(BodyTypeErrorStatus.JSON_PARSING_ERROR);
        }
    }
}
