package com.example.mody.domain.bodytype.entity;

import com.example.mody.global.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "body_type")
public class BodyType extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "body_type_id")
    private Long id;

    @Column(columnDefinition = "varchar(30)", nullable = false)
    private String name;

}
