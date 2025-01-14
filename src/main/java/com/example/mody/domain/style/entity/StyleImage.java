package com.example.mody.domain.style.entity;

import com.example.mody.global.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class StyleImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "style_image_id")
    private Long id;

    private String image_url;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "style_recommendation_id")
    private Style style;

    public StyleImage(String image_url, Style style){

        this.image_url = image_url;
        this.style = style;
    }
}
