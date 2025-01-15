package com.example.mody.domain.style.service;

import com.example.mody.domain.style.dto.response.CategoryResponse;
import com.example.mody.domain.style.repository.AppealCategoryRepository;
import com.example.mody.domain.style.repository.StyleCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StyleQueryServiceImpl implements StyleQueryService{

    private final StyleCategoryRepository styleCategoryRepository;
    private final AppealCategoryRepository appealCategoryRepository;

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
}
