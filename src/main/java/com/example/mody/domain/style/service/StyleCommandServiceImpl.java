package com.example.mody.domain.style.service;

import com.example.mody.domain.chatgpt.service.ChatGptService;
import com.example.mody.domain.style.dto.request.StyleRecommendRequest;
import com.example.mody.domain.style.dto.response.StyleRecommendResponse;
import com.example.mody.domain.style.entity.Style;
import com.example.mody.domain.style.repository.StyleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class StyleCommandServiceImpl implements StyleCommandService{

    private final StyleRepository styleRepository;
    private final ChatGptService chapGptService;

    //gptService로 답변 받아오고 데이터를 디비에 저장
    @Override
    public StyleRecommendResponse recommendStyle(StyleRecommendRequest request) {

        //ChatGptService를 호출하여 스타일 추천 데이터를 가져옴
        StyleRecommendResponse response = chapGptService.recommendStyle(request);

        //데이터 저장 로직

        return response;
    }
}
