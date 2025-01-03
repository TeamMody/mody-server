package com.example.mody.domain.member.entity;

import com.example.mody.domain.member.enums.Gender;
import com.example.mody.domain.member.enums.Role;
import com.example.mody.domain.member.enums.Status;
import com.example.mody.global.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

@Entity
@Getter
@DynamicUpdate
@DynamicInsert
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(length = 10, nullable = false)
    private String nickname;

    private String profileUrl;

    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private Integer height;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10) DEFAULT 'ACTIVE'")
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(15) DEFAULT 'USER'")
    private Role role;

    public void encodePassword(String password) {
        this.password = password;
    }
}
