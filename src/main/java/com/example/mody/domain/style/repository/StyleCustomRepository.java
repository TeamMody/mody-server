package com.example.mody.domain.style.repository;

import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.style.dto.response.StyleRecommendResponses;


public interface StyleCustomRepository {

    StyleRecommendResponses findMyStyleRecommends(Member member, Integer size, Long Cursor);
}
