package com.example.mody.domain.style.dto.response;

import lombok.Getter;

import java.util.List;

@Getter
public class CategoryResponse {

    private List<String> styleCategories;

    private List<String> appealCategories;
}
