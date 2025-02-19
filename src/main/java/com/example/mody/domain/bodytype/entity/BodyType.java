package com.example.mody.domain.bodytype.entity;

import com.example.mody.domain.bodytype.entity.mapping.MemberBodyType;
import com.example.mody.domain.member.entity.Member;
import com.example.mody.global.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    @Column(name = "body_type_id")
    private Long id;

    @Column(nullable = false, length = 30)
    private String name; // 네추럴, 스트레이트, 웨이브

    @OneToMany(mappedBy = "bodyType", cascade = CascadeType.ALL)
    private List<MemberBodyType> memberBodyTypeList = new ArrayList<>();

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BodyType that)) {
            return false;
        }
        return Objects.equals(that.getId(), this.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}