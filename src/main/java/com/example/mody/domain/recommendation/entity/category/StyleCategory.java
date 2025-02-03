package com.example.mody.domain.recommendation.entity.category;

import com.example.mody.global.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class StyleCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "style_category_id")
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;
}
