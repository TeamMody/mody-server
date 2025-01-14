package com.example.mody.domain.style.repository;

import com.example.mody.domain.style.dto.response.StyleRecommendResponse;
import com.example.mody.domain.style.entity.Style;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StyleRepository extends JpaRepository<Style, Long> {
}
