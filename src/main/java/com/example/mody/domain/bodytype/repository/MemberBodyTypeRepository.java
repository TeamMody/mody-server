package com.example.mody.domain.bodytype.repository;

import com.example.mody.domain.bodytype.entity.mapping.MemberBodyType;
import com.example.mody.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberBodyTypeRepository extends JpaRepository<MemberBodyType, Long> {
    Optional<MemberBodyType> findTopByMemberOrderByCreatedAt(Member member);
    Optional<MemberBodyType> findMemberBodyTypeByMember(Member member);
    Long countAllByMember(Member member);

}
