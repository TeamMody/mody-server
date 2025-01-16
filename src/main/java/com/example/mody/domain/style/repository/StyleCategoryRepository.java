package com.example.mody.domain.style.repository;

import com.example.mody.domain.style.entity.category.StyleCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StyleCategoryRepository extends JpaRepository<StyleCategory, Long> {
}
