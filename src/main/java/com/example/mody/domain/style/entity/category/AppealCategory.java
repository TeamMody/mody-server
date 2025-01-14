package com.example.mody.domain.style.entity.category;

import com.example.mody.global.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class AppealCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appeal_category_id")
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;
}
