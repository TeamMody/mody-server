package com.example.mody.domain.style.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mody.domain.style.entity.Style;

public interface StyleRepository extends JpaRepository<Style, Long>, StyleCustomRepository {

	List<Style> findByMemberId(Long memberId);
}
