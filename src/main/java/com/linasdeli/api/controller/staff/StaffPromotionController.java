package com.linasdeli.api.controller.staff;

import com.linasdeli.api.domain.Promotion;
import com.linasdeli.api.service.PromotionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
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
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public ResponseEntity<Promotion> createPromotion(
            @RequestParam(value = "promotionTitle", required = false) String promotionTitle,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {

        // Convert strings to LocalDateTime
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        LocalDateTime startDateTime = start.atStartOfDay();             // 00:00:00
        LocalDateTime endDateTime = end.atTime(23, 59, 59);             // 23:59:59

        Promotion promotion = promotionService.createPromotion(promotionTitle, startDateTime, endDateTime, image);
        return new ResponseEntity<>(promotion, HttpStatus.CREATED);
    }

    // ✅ 프로모션 목록 조회 (페이징 및 검색)
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public ResponseEntity<Page<Promotion>> getPromotions(
            Pageable pageable,
            @RequestParam(value = "keyword", required = false) String keyword) {
        log.info("Searching promotions with keyword: {}", keyword);  // 올바른 방식으로 수정
        Page<Promotion> promotions = promotionService.getPromotions(pageable, keyword);
        return new ResponseEntity<>(promotions, HttpStatus.OK);
    }

    // ✅ 프로모션 상세 조회
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public ResponseEntity<Promotion> getPromotionById(@PathVariable Long id) {
        Promotion promotion = promotionService.getPromotionById(id);
        return new ResponseEntity<>(promotion, HttpStatus.OK);
    }

    // ✅ 프로모션 업데이트
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public ResponseEntity<Promotion> updatePromotion(
            @PathVariable Long id,
            @RequestParam(value = "promotionTitle", required = false) String promotionTitle,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {

        LocalDateTime start = (startDate != null) ? LocalDate.parse(startDate).atStartOfDay() : null;
        LocalDateTime end = (endDate != null) ? LocalDate.parse(endDate).atTime(23, 59, 59) : null;

        Promotion promotion = promotionService.updatePromotion(id, promotionTitle, start, end, image);
        return new ResponseEntity<>(promotion, HttpStatus.OK);
    }

    // ✅ 프로모션 삭제
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_STAFF')")
    public ResponseEntity<Void> deletePromotion(@PathVariable Long id) {
        promotionService.deletePromotion(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
