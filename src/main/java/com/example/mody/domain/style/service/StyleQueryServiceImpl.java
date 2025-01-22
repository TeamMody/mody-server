package com.example.mody.domain.style.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.style.dto.response.CategoryResponse;
import com.example.mody.domain.style.dto.response.StyleRecommendResponse;
import com.example.mody.domain.style.entity.Style;
import com.example.mody.domain.style.exception.StyleException;
import com.example.mody.domain.style.repository.AppealCategoryRepository;
import com.example.mody.domain.style.repository.StyleCategoryRepository;
import com.example.mody.domain.style.repository.StyleRepository;
import com.example.mody.global.common.exception.code.status.StyleErrorStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StyleQueryServiceImpl implements StyleQueryService {

	private final StyleCategoryRepository styleCategoryRepository;
	private final AppealCategoryRepository appealCategoryRepository;
	private final StyleRepository styleRepository;

	@Override
	public CategoryResponse getCategories() {

		//styleCategories 조회 및 이름 리스트로 반환
		List<String> styleCategories = styleCategoryRepository.findAll()
			.stream()
			.map(styleCategory -> styleCategory.getName())
			.collect(Collectors.toList());

		List<String> appealCategories = appealCategoryRepository.findAll()
			.stream()
			.map(appealCategory -> appealCategory.getName())
			.collect(Collectors.toList());

		return new CategoryResponse(styleCategories, appealCategories);
	}

	@Override
	public StyleRecommendResponse getRecommendedStyle(Member member) {
		Style style = styleRepository.findByMemberId(member.getId())
			.orElseThrow(() -> new StyleException(StyleErrorStatus.STYLE_NOT_FOUND));

		return StyleRecommendResponse.of(member.getNickname(), StyleRecommendResponse.StyleRecommendation.from(style));
	}
}
