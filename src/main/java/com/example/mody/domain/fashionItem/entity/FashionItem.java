package com.example.mody.domain.fashionItem.entity;

import com.example.mody.domain.member.entity.Member;
import com.example.mody.global.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FashionItem extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fashion_item_id")
    private Long id;

    @Column(length = 300, nullable = false)
    private String item;

    @Column(length = 1000, nullable = false)
    private String description;

    @Column(length = 300, nullable = false)
    private String imageUrl;

    @Builder.Default
    private boolean isLiked = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void toggleLike() {
        this.isLiked = !this.isLiked;
    }
}
