package com.example.mody.domain.recommendation.entity.mapping;

import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.recommendation.entity.Recommendation;
import com.example.mody.global.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member_recommendation_like")
public class MemberRecommendationLike extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_recommendation_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recommendation_id")
    private Recommendation recommendation;

    public MemberRecommendationLike(Member member, Recommendation recommendation){
        this.member = member;
        this.recommendation = recommendation;
    }
}
