package com.example.mody.domain.post.repository;

import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.post.entity.Post;
import com.example.mody.domain.post.entity.mapping.PostReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostReportRepository extends JpaRepository<PostReport, Long> {
    boolean existsByMemberAndPost(Member member, Post post);
    void deleteByPost(Post post);
}
