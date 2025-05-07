package com.linasdeli.api.service;

import com.linasdeli.api.domain.Promotion;
import com.linasdeli.api.repository.PromotionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import java.util.List;

@Slf4j
@Service
public class PromotionService {

    private final PromotionRepository promotionRepository;
    // 업로드 경로
    private final String uploadDir = "upload";

    // 도메인 주소 (localhost 또는 배포 주소)
    @Value("${server.domain}")
    private String serverDomain;

    @Autowired
    public PromotionService(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }


    // 프로모션 생성 메서드
    public Promotion createPromotion(String promotionTitle, LocalDateTime startDate, LocalDateTime endDate, MultipartFile image) throws IOException {
        Promotion promotion = new Promotion();
        promotion.setStartDate(startDate);
        promotion.setEndDate(endDate);

        // ✅ 제목 자동 생성 vs 수동 입력 처리
        if (promotionTitle == null || promotionTitle.trim().isEmpty()) {
            promotion.setPromotionTitle(generatePromotionTitle(startDate, endDate));
        } else {
            promotion.setPromotionTitle(promotionTitle);
        }

        // ✅ 이미지 저장 처리
        // 이미지 처리
        if (image != null && !image.isEmpty()) {
            String fileName = storeImage(image);
            promotion.setPromotionImageUrl("/upload/" + fileName);  // ✅ 절대 URL 저장
        }

        // ✅ 시간 설정
        promotion.setCreatedAt(LocalDateTime.now());
        promotion.setUpdatedAt(LocalDateTime.now());

        return promotionRepository.save(promotion);
    }

    // startDate와 endDate를 기반으로 제목을 생성하는 메서드
    private String generatePromotionTitle(LocalDateTime startDate, LocalDateTime endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedStartDate = startDate.format(formatter);
        String formattedEndDate = endDate.format(formatter);

        return "Promotion from " + formattedStartDate + " to " + formattedEndDate;
    }
    private String storeImage(MultipartFile image) throws IOException {
        // 파일 확장자 추출
        String originalName = image.getOriginalFilename();
        String extension = originalName.substring(originalName.lastIndexOf("."));

        // 파일명 생성: UUID 앞 8자리 + "_" + 원래 이름
        String uuidPrefix = UUID.randomUUID().toString().substring(0, 8);
        String fileName = uuidPrefix + "_" + originalName;

        Path uploadPath = Paths.get(uploadDir, fileName);
        if (!Files.exists(Paths.get(uploadDir))) {
            Files.createDirectories(Paths.get(uploadDir));
        }

        Files.write(uploadPath, image.getBytes());

        return fileName; // ex) 0f78ddf2_profile.png
    }

    // Promotion 목록 페이징 및 검색
    public Page<Promotion> getPromotions(Pageable pageable, String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return promotionRepository.findAll(pageable);  // 검색어가 없으면 전체 목록을 반환
        } else {
            return promotionRepository.findByPromotionTitleContainingIgnoreCase(keyword, pageable); // 제목에서 키워드 포함된 것만 반환
        }
    }

    // 프로모션 상세 정보 가져오기
    public Promotion getPromotionById(Long promotionId) {
        return promotionRepository.findById(promotionId)
                .orElseThrow(() -> new EntityNotFoundException("Promotion not found with id: " + promotionId));
    }

    public Promotion updatePromotion(Long promotionId, String promotionTitle, LocalDateTime startDate, LocalDateTime endDate, MultipartFile image) throws IOException {
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new EntityNotFoundException("Promotion with id " + promotionId + " not found"));

        // 제목 변경
        if (promotionTitle != null && !promotionTitle.trim().isEmpty()) {
            promotion.setPromotionTitle(promotionTitle);
        }

        // 날짜 변경
        if (startDate != null) {
            promotion.setStartDate(startDate);
        }
        if (endDate != null) {
            promotion.setEndDate(endDate);
        }

        // 이미지 변경
        if (image != null && !image.isEmpty()) {
            String fileName = storeImage(image);
            promotion.setPromotionImageName(fileName);
            promotion.setPromotionImageUrl("/upload/" + fileName);
        }

        return promotionRepository.save(promotion);
    }

    public void deletePromotion(Long id) {
        promotionRepository.deleteById(id);
    }

    public List<Promotion> getActivePromotions() {
        return promotionRepository.findActivePromotions(LocalDateTime.now());
    }



}
