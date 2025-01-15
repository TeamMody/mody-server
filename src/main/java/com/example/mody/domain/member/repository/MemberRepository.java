package com.example.mody.domain.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.member.enums.LoginType;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByEmail(String email);

	Boolean existsByEmail(String email);

	Optional<Member> findByProviderId(String providerId);

	Optional<Member> findByEmailAndLoginType(String email, LoginType loginType);

	Optional<Member> findByProviderIdAndLoginType(String providerId, LoginType loginType);

	Boolean existsByEmailAndLoginType(String email, LoginType loginType);

}
