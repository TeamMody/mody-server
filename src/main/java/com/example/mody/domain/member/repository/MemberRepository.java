package com.example.mody.domain.member.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.member.enums.LoginType;
import com.example.mody.domain.member.enums.Status;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByEmail(String email);

	Boolean existsByEmail(String email);

	Optional<Member> findByProviderId(String providerId);

	Optional<Member> findByEmailAndLoginType(String email, LoginType loginType);

	Optional<Member> findByProviderIdAndLoginType(String providerId, LoginType loginType);

	Boolean existsByEmailAndLoginType(String email, LoginType loginType);

	List<Member> findByStatusAndDeletedAtBefore(Status status, LocalDateTime deletedAt);
}
