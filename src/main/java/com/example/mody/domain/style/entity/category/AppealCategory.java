package com.example.mody.domain.style.entity.category;

import com.example.mody.global.common.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class AppealCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appeal_category_id")
    private Long id;

    @Column(length = 10, nullable = false)
    private String name;
}
