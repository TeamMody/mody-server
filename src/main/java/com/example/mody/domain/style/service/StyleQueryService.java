package com.example.mody.domain.style.service;

import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.style.dto.response.CategoryResponse;
import com.example.mody.domain.style.dto.response.StyleRecommendResponse;
import com.example.mody.domain.style.dto.response.StyleRecommendResponses;

public interface StyleQueryService {

	CategoryResponse getCategories();

	StyleRecommendResponses getRecommendedStyle(Member member, Long cursor, Integer size);
	
}
