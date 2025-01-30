package com.example.mody.domain.style.entity;

import com.example.mody.domain.member.entity.Member;
import com.example.mody.global.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Style extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "style_recommendation_id")
    private Long id;

    @Column(length = 1000, nullable = false)
    private String recommendedStyle;

    @Column(length = 1000, nullable = false)
    private String introduction;

    @Column(length = 1000, nullable = false)
    private String styleDirection;

    @Column(length = 1000, nullable = false)
    private String practicalStylingTips;

    @Column(length = 300, nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private Integer likeCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Style(String recommendedStyle, String introduction, String styleDirection, String practicalStylingTips, Member member, String imageUrl) {

        this.recommendedStyle = recommendedStyle;
        this.introduction = introduction;
        this.styleDirection = styleDirection;
        this.practicalStylingTips = practicalStylingTips;
        this.member = member;
        this.imageUrl = imageUrl;
        this.likeCount = 0;
    }
}
