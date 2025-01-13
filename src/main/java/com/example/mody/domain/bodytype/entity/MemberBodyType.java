package com.example.mody.domain.bodytype.entity;

import com.example.mody.domain.member.entity.Member;
import com.example.mody.global.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "member_body_type")
@Getter
public class MemberBodyType extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_body_type_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "body_type_id", nullable = false)
    private BodyType bodyType;

    @Column(length = 2000)
    private String content;
}
