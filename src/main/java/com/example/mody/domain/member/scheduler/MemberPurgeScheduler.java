package com.example.mody.domain.member.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.member.enums.Status;
import com.example.mody.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberPurgeScheduler {

	private final MemberRepository memberRepository;

	// 매일 자정에 실행 (cron 표현식은 환경에 맞게 수정하세요)
	@Scheduled(cron = "0 0 0 * * *")
	@Transactional
	public void purgeDeletedMembers() {
		LocalDateTime deleteAt = LocalDateTime.now().minusMonths(1);
		List<Member> membersToPurge = memberRepository.findByStatusAndDeletedAtBefore(Status.DELETED, deleteAt);
		if (!membersToPurge.isEmpty()) {
			log.info("Purging {} member(s) deleted before {}", membersToPurge.size(), deleteAt);
			memberRepository.deleteAll(membersToPurge);
		}
	}
}
