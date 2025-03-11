package com.linasdeli.api.repository;

import com.linasdeli.api.domain.Promotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {

    // 프로모션 제목에서 키워드를 포함하는 프로모션을 검색 (대소문자 구분 없이)
    Page<Promotion> findByPromotionTitleContainingIgnoreCase(String keyword, Pageable pageable);


}