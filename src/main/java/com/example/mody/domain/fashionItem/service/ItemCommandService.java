package com.example.mody.domain.fashionItem.service;

import com.example.mody.domain.fashionItem.dto.request.FashionItemRequest;
import com.example.mody.domain.fashionItem.dto.response.ItemRecommendResponse;
import com.example.mody.domain.fashionItem.dto.response.ItemsResponse;
import com.example.mody.domain.member.entity.Member;

public interface ItemCommandService {

    public ItemRecommendResponse recommendItem(FashionItemRequest request, Member member);
}
