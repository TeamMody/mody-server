package com.example.mody.domain.post.repository;

import com.example.mody.domain.post.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
    @Query("SELECT pi.id FROM PostImage pi WHERE pi.post.id = :postId")
    List<Long> findPostImageIdByPostId(@Param("postId") Long postId);
}
