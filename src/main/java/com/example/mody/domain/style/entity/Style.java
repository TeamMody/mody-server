package com.example.mody.domain.style.entity;

import com.example.mody.domain.member.entity.Member;
import com.example.mody.global.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(mappedBy = "style", cascade = CascadeType.ALL)
    private StyleImage styleImage;

    public Style(String recommendedStyle, String introduction, String styleDirection, String practicalStylingTips, Member member) {

        this.recommendedStyle = recommendedStyle;
        this.introduction = introduction;
        this.styleDirection = styleDirection;
        this.practicalStylingTips = practicalStylingTips;
        this.member = member;
    }

    public void setStyleImage(StyleImage styleImage) {
        this.styleImage = styleImage;
    }
}
