package com.example.mody.domain.bodytype.repository;

import com.example.mody.domain.bodytype.entity.BodyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BodyTypeRepository extends JpaRepository<BodyType, Long> {
}
