package com.example.mody.domain.style.service;

import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.style.dto.response.CategoryResponse;
import com.example.mody.domain.style.dto.response.StyleRecommendResponse;

public interface StyleQueryService {

	CategoryResponse getCategories();

	StyleRecommendResponse getRecommendedStyle(Member member);
	
}
