package com.linasdeli.api.controller.staff;

import com.linasdeli.api.domain.Promotion;
import com.linasdeli.api.service.PromotionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/api/staff/promotions")
public class StaffPromotionController {

    private final PromotionService promotionService;

    @Autowired
    public StaffPromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    // ✅ 프로모션 생성
    @PostMapping
    public ResponseEntity<Promotion> createPromotion(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {

        // Convert strings to LocalDateTime
        LocalDateTime start = LocalDateTime.parse(startDate);
        LocalDateTime end = LocalDateTime.parse(endDate);

        Promotion promotion = promotionService.createPromotion(start, end, image);
        return new ResponseEntity<>(promotion, HttpStatus.CREATED);
    }

    // ✅ 프로모션 목록 조회 (페이징 및 검색)
    @GetMapping
    public ResponseEntity<Page<Promotion>> getPromotions(
            Pageable pageable,
            @RequestParam(value = "keyword", required = false) String keyword) {
        log.info("Searching promotions with keyword: {}", keyword);  // 올바른 방식으로 수정
        Page<Promotion> promotions = promotionService.getPromotions(pageable, keyword);
        return new ResponseEntity<>(promotions, HttpStatus.OK);
    }

    // ✅ 프로모션 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<Promotion> getPromotionById(@PathVariable Long id) {
        Promotion promotion = promotionService.getPromotionById(id);
        return new ResponseEntity<>(promotion, HttpStatus.OK);
    }

    // ✅ 프로모션 업데이트
    @PutMapping("/{id}")
    public ResponseEntity<Promotion> updatePromotion(
            @PathVariable Long id,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {

        LocalDateTime start = (startDate != null) ? LocalDateTime.parse(startDate) : null;
        LocalDateTime end = (endDate != null) ? LocalDateTime.parse(endDate) : null;

        Promotion promotion = promotionService.updatePromotion(id, start, end, image);
        return new ResponseEntity<>(promotion, HttpStatus.OK);
    }

    // ✅ 프로모션 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePromotion(@PathVariable Long id) {
        promotionService.deletePromotion(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
