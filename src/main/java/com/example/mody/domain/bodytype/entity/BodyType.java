package com.example.mody.domain.bodytype.entity;

import com.example.mody.domain.bodytype.entity.mapping.MemberBodyType;
import com.example.mody.global.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@DynamicUpdate
@DynamicInsert
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BodyType extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String name; // 네추럴, 스트레이트, 웨이브

    @OneToMany(mappedBy = "bodyType", cascade = CascadeType.ALL)
    private List<MemberBodyType> memberBodyTypeList = new ArrayList<>();
}