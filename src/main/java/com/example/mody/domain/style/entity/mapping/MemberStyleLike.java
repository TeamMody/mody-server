package com.example.mody.domain.style.entity.mapping;

import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.style.entity.Style;
import com.example.mody.global.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member_style_like")
public class MemberStyleLike extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_style_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "style_id")
    private Style style;

    public MemberStyleLike(Member member, Style style){
        this.member = member;
        this.style = style;
    }
}
