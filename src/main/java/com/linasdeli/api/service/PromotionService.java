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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
@Service
public class PromotionService {

    private final PromotionRepository promotionRepository;
    private final String uploadDir = "upload";  // 업로드할 로컬 디렉토리 경로 설정

    @Autowired
    public PromotionService(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    // 프로모션 생성 메서드
    public Promotion createPromotion(LocalDateTime startDate, LocalDateTime endDate, MultipartFile image) throws IOException {
        // Promotion 객체 생성
        Promotion promotion = new Promotion();
        promotion.setStartDate(startDate);
        promotion.setEndDate(endDate);
        promotion.setPromotionTitle(generatePromotionTitle(startDate, endDate));  // 제목 자동 생성

        // 이미지 처리
        if (image != null) {
            String fileName = storeImage(image); // 로컬 디렉토리에 저장된 이미지의 파일 이름 반환
            promotion.setPromotionImageName(fileName);
            promotion.setPromotionImageUrl("/upload/" + fileName); // 로컬 URL 경로
        }

        // 프로모션 저장
        return promotionRepository.save(promotion);
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

    public Promotion updatePromotion(Long promotionId, LocalDateTime startDate, LocalDateTime endDate, MultipartFile image) throws IOException {
        // 프로모션을 찾는다
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new EntityNotFoundException("Promotion with id " + promotionId + " not found"));

        // 주어진 필드만 변경
        if (startDate != null) {
            promotion.setStartDate(startDate);
        }
        if (endDate != null) {
            promotion.setEndDate(endDate);
        }

        // 이미지 처리
        if (image != null) {
            String fileName = storeImage(image); // 로컬 디렉토리에 저장된 이미지의 파일 이름 반환
            promotion.setPromotionImageName(fileName);
            promotion.setPromotionImageUrl("/upload/" + fileName); // 로컬 URL 경로
        }

        // 변경된 프로모션 저장
        return promotionRepository.save(promotion);
    }

    public void deletePromotion(Long id) {
        promotionRepository.deleteById(id);
    }

    // startDate와 endDate를 기반으로 제목을 생성하는 메서드
    private String generatePromotionTitle(LocalDateTime startDate, LocalDateTime endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedStartDate = startDate.format(formatter);
        String formattedEndDate = endDate.format(formatter);

        return "Promotion from " + formattedStartDate + " to " + formattedEndDate;
    }

    // 로컬 디렉토리에 이미지를 저장하는 메서드
    private String storeImage(MultipartFile image) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
        Path path = Paths.get(uploadDir, fileName);

        // 로컬 디렉토리가 없으면 생성
        if (!Files.exists(Paths.get(uploadDir))) {
            Files.createDirectories(Paths.get(uploadDir));
        }

        // 파일을 로컬에 저장
        Files.write(path, image.getBytes());

        return fileName; // 저장된 파일 이름 반환
    }
}

