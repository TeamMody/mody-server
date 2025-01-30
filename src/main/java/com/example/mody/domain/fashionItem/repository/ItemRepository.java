package com.example.mody.domain.fashionItem.repository;

import com.example.mody.domain.fashionItem.entity.FashionItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<FashionItem ,Long> {

    @Query("""
        SELECT fi FROM FashionItem fi
        WHERE fi.member.id = :memberId
        AND (:cursor IS NULL OR fi.id <= :cursor)
        ORDER BY fi.id DESC
        LIMIT :size
    """)
    List<FashionItem> findRecommendedItems(@Param("memberId") Long memberId,
                                           @Param("cursor") Long cursor,
                                           @Param("size") int size);

    Optional<FashionItem> findByIdAndMemberId(Long fashionItemId, Long memberId);
}
