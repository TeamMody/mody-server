package com.example.mody.domain.fashionItem.repository;

import com.example.mody.domain.fashionItem.entity.FashionItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FashionItemRepository extends JpaRepository<FashionItem ,Long> {
}
