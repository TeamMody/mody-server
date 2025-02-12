package com.example.mody.domain.recommendation.entity;

import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.recommendation.entity.mapping.MemberRecommendationLike;
import com.example.mody.domain.recommendation.enums.RecommendType;
import com.example.mody.global.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Table(name = "recommendation")
public class Recommendation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recommendation_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private RecommendType recommendType;


    private String title;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(length = 300, nullable = false)
    private String imageUrl;

    @Builder.Default
    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer likeCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "recommendation",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<MemberRecommendationLike> RecommendLikes = new ArrayList<>();

    public void increaseLikeCount() {
        this.likeCount++;
    }

    public void decreaseLikeCount() {
        this.likeCount--;
    }

    public static Recommendation of(RecommendType recommendType, String title, String content, String imageUrl, Member member) {

        return Recommendation.builder()
                .recommendType(recommendType)
                .title(title)
                .content(content)
                .imageUrl(imageUrl)
                .member(member)
                .build();
    }
}
