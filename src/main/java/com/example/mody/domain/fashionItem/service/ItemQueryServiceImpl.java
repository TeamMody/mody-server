package com.example.mody.domain.fashionItem.service;

import com.example.mody.domain.fashionItem.dto.response.ItemsResponse;
import com.example.mody.domain.fashionItem.entity.FashionItem;
import com.example.mody.domain.fashionItem.repository.ItemRepository;
import com.example.mody.domain.member.entity.Member;
import com.example.mody.global.common.exception.RestApiException;
import com.example.mody.global.common.exception.code.status.GlobalErrorStatus;
import com.example.mody.global.dto.response.CursorPagination;
import com.example.mody.global.dto.response.CursorResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemQueryServiceImpl implements ItemQueryService{

    private final ItemRepository itemRepository;

    @Override
    public CursorResult<ItemsResponse> getRecommendedItems(Member member, Long cursor, int size) {

        if(size<=0){
            throw new RestApiException(GlobalErrorStatus.NEGATIVE_PAGE_SIZE_REQUEST);
        }

        //추천 받은 아이템들 가져오기
        List<FashionItem> fashionItems = itemRepository.findRecommendedItems(member.getId(), cursor, size + 1);

        boolean hasNext = fashionItems.size() > size;

        List<FashionItem> resultList = hasNext ? fashionItems.subList(0, size) : fashionItems;

        //마지막 아이템 id 계산
        Long nextCursor = fashionItems.isEmpty() ? null : fashionItems.get(fashionItems.size() - 1).getId();

        ItemsResponse response = ItemsResponse.of(member.getNickname(), resultList);

        return new CursorResult<>(response, new CursorPagination(hasNext, nextCursor));
    }
}
