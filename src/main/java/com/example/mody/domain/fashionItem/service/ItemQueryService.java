package com.example.mody.domain.fashionItem.service;

import com.example.mody.domain.fashionItem.dto.response.ItemsResponse;
import com.example.mody.domain.member.entity.Member;
import com.example.mody.global.dto.response.CursorResult;

public interface ItemQueryService {

    public CursorResult<ItemsResponse> getRecommendedItems(Member memer, Long cursor, int size);
}
