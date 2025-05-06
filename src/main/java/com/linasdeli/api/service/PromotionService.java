package com.linasdeli.api.service;

import com.linasdeli.api.domain.Promotion;
import com.linasdeli.api.repository.PromotionRepository;
import com.linasdeli.api.util.FileUtil;
import com.linasdeli.api.util.FileUtil.UploadResult;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
public class PromotionService {

    private final PromotionRepository promotionRepository;
    private final FileUtil fileUtil;

    @Value("${server.domain}")
    private String serverDomain;

    @Autowired
    public PromotionService(PromotionRepository promotionRepository, FileUtil fileUtil) {
        this.promotionRepository = promotionRepository;
        this.fileUtil = fileUtil;
    }

    // 프로모션 생성
    public Promotion createPromotion(String promotionTitle, LocalDateTime startDate, LocalDateTime endDate, MultipartFile image) throws IOException {
        Promotion promotion = new Promotion();
        promotion.setStartDate(startDate);
        promotion.setEndDate(endDate);
        promotion.setPromotionTitle(
                (promotionTitle == null || promotionTitle.trim().isEmpty())
                        ? generatePromotionTitle(startDate, endDate)
                        : promotionTitle
        );

        if (image != null && !image.isEmpty()) {
            UploadResult result = fileUtil.saveImage(image, "promotion");
            promotion.setPromotionImageName(result.getFileName());
            promotion.setPromotionImageUrl(result.getUrl());
        }

        promotion.setCreatedAt(LocalDateTime.now());
        promotion.setUpdatedAt(LocalDateTime.now());
        return promotionRepository.save(promotion);
    }

    // 제목 자동 생성
    private String generatePromotionTitle(LocalDateTime startDate, LocalDateTime endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return "Promotion from " + startDate.format(formatter) + " to " + endDate.format(formatter);
    }

    // 프로모션 목록
    public Page<Promotion> getPromotions(Pageable pageable, String keyword) {
        // 기본 정렬이 없는 경우 startDate 내림차순으로 설정
        if (!pageable.getSort().isSorted()) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("startDate").descending());
        }

        if (keyword == null || keyword.isEmpty()) {
            return promotionRepository.findAll(pageable);
        } else {
            return promotionRepository.findByPromotionTitleContainingIgnoreCase(keyword, pageable);
        }
    }

    // 프로모션 단건 조회
    public Promotion getPromotionById(Long promotionId) {
        return promotionRepository.findById(promotionId)
                .orElseThrow(() -> new EntityNotFoundException("Promotion not found with id: " + promotionId));
    }

    // 프로모션 업데이트
    public Promotion updatePromotion(Long promotionId, String promotionTitle, LocalDateTime startDate, LocalDateTime endDate, MultipartFile image) throws IOException {
        Promotion promotion = getPromotionById(promotionId);

        if (promotionTitle != null && !promotionTitle.trim().isEmpty()) {
            promotion.setPromotionTitle(promotionTitle);
        }
        if (startDate != null) promotion.setStartDate(startDate);
        if (endDate != null) promotion.setEndDate(endDate);

        if (image != null && !image.isEmpty()) {
            UploadResult result = fileUtil.saveImage(image, "promotion");
            promotion.setPromotionImageName(result.getFileName());
            promotion.setPromotionImageUrl(result.getUrl());
        }

        promotion.setUpdatedAt(LocalDateTime.now());
        return promotionRepository.save(promotion);
    }

    public void deletePromotion(Long id) {
        promotionRepository.deleteById(id);
    }

    public List<Promotion> getActivePromotions() {
        return promotionRepository.findActivePromotions(LocalDateTime.now());
    }
}