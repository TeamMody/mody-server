package com.example.mody.domain.post.repository;

import com.example.mody.domain.member.entity.Member;
import com.example.mody.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostCustomRepository {
}
