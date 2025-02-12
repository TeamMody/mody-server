package com.example.mody.domain.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mody.domain.auth.entity.RefreshToken;
import com.example.mody.domain.member.entity.Member;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
	Optional<RefreshToken> findByToken(String token);

	Optional<RefreshToken> findByMember(Member member);

	boolean existsByToken(String token);
}