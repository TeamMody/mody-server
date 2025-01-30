package com.example.mody.domain.fashionItem.service;

import com.example.mody.domain.fashionItem.dto.request.FashionItemRequest;
import com.example.mody.domain.fashionItem.dto.response.FashionItemRecommendResponse;
import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.style.dto.BodyTypeDTO;

public interface FashionItemCommandService {

    public FashionItemRecommendResponse recommendItem(FashionItemRequest request, Member member);
}
