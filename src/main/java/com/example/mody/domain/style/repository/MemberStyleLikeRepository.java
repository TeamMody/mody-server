package com.example.mody.domain.style.repository;

import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.style.entity.Style;
import com.example.mody.domain.style.entity.mapping.MemberStyleLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberStyleLikeRepository extends JpaRepository<MemberStyleLike, Long> {
    Optional<MemberStyleLike> findTopByMemberAndStyle(Member member, Style style);
}
