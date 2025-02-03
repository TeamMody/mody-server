package com.example.mody.domain.recommendation.repository;

import com.example.mody.domain.recommendation.entity.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long>, RecommendCustomRepository {
}
