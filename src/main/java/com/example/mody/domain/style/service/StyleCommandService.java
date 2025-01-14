package com.example.mody.domain.style.service;

import com.example.mody.domain.style.dto.request.StyleRecommendRequest;
import com.example.mody.domain.style.dto.response.StyleRecommendResponse;

public interface StyleCommandService {

    StyleRecommendResponse recommendStyle(StyleRecommendRequest request);
}
