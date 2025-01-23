package com.example.mody.domain.style.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mody.domain.style.entity.Style;

public interface StyleRepository extends JpaRepository<Style, Long> {

	Optional<Style> findByMemberId(Long memberId);
}
