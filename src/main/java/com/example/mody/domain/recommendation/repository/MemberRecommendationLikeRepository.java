package com.example.mody.domain.recommendation.repository;

import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.recommendation.entity.Recommendation;
import com.example.mody.domain.recommendation.entity.mapping.MemberRecommendationLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRecommendationLikeRepository extends JpaRepository<MemberRecommendationLike, Long> {
    Optional<MemberRecommendationLike> findTopByMemberAndRecommendation(Member member, Recommendation recommendation);
}
